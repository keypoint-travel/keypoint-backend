package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.premium.dto.apple.AppleAppStoreResponse;
import com.keypoint.keypointtravel.premium.dto.apple.InApp;
import com.keypoint.keypointtravel.premium.dto.useCase.ApplePurchaseUseCase;
import com.keypoint.keypointtravel.premium.dto.useCase.AppleReceiptUseCase;
import com.keypoint.keypointtravel.premium.entity.ApplePurchaseHistory;
import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import com.keypoint.keypointtravel.premium.repository.ApplePurchaseHistoryRepository;
import com.keypoint.keypointtravel.premium.repository.MemberPremiumRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AppleService {

    private final ApplePurchaseApiService applePurchaseApiService;

    private final ApplePurchaseApiTestService applePurchaseApiTestService;

    private final ApplePurchaseHistoryRepository applePurchaseHistoryRepository;

    private final MemberRepository memberRepository;

    private final MemberPremiumRepository memberPremiumRepository;

    /**
     * 영수증 검증 함수
     *
     * @Param 영수증 데이터 useCase
     * @Return 영수증에 해당하는 결제 정보 response
     */
    @Transactional
    public AppleAppStoreResponse verifyReceipt(AppleReceiptUseCase useCase) {
        HashMap<String, String> appStoreRequest = new HashMap<>();
        appStoreRequest.put("receipt-data", useCase.getReceiptData());
        // 애플 서버에 영수증 검증 요청
        AppleAppStoreResponse response = applePurchaseApiService.verifyReceipt(appStoreRequest);
        // 21007: 애플 테스트 환경에서 구매한 영수증이 실제 환경에서 검증되는 경우
        if (response.getStatus() == 21007) {
            response = applePurchaseApiTestService.verifyReceiptTest(appStoreRequest);
        }
        // 애플 서버 응답 상태 코드에 따른 처리
        else if (response.getStatus() != 0) {
            verifyStatusCode(response.getStatus());
        }
        // 영수증 검증 성공 시 응답 반환
        return response;
    }

    /**
     * 소모품 (Consumable) 구매 기록 업데이트 및 프리미엄 적용 함수
     *
     * @Param memberId, amount, currency, 결제 정보 useCase
     */
    @Transactional
    public void updatePurchaseHistory(ApplePurchaseUseCase useCase) {
        // 애플 앱 스토어 결제 성공 시, 결제 정보를 DB에 저장
        InApp inApp = useCase.getAppleAppStoreResponse().getReceipt().getInApp().get(0);
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        ApplePurchaseHistory history = ApplePurchaseHistory.builder()
            .member(member)
            .productId(inApp.getProductId())
            .transactionId(inApp.getTransactionId())
            .originalTransactionId(inApp.getOriginalTransactionId())
            .amount(useCase.getAmount())
            .currency(useCase.getCurrency())
            .purchasedAt(new Date(Long.parseLong(inApp.getPurchaseDateMs())))
            .purchaseStatus(PurchaseStatus.COMPLETED)
            .build();
        try {
            applePurchaseHistoryRepository.save(history);
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
        // 회원 프리미엄 적용
        Optional<MemberPremium> memberPremium = memberPremiumRepository.findByMemberId(
            useCase.getMemberId());
        // 이미 적용했던 기록이 있는 경우
        if (memberPremium.isPresent()) {
            // 7일간 무료권 적용중인 경우
            if (memberPremium.get().isFree()) {
                memberPremium.get().updateIsFree(false);
                memberPremium.get().updateStartedAt(LocalDateTime.now());
                memberPremium.get().updateExpirationAt(LocalDateTime.now().plusMonths(12));
            } else {
                // 유료 프리미엄이 적용중인 경우
                if (memberPremium.get().isActive()) {
                    memberPremium.get().updateExpirationAt(
                        memberPremium.get().getExpirationAt().plusMonths(12));
                }
                // 프리미엄 만료일이 지난 경우
                else {
                    memberPremium.get().updateIsActive(true);
                    memberPremium.get().updateStartedAt(LocalDateTime.now());
                    memberPremium.get().updateExpirationAt(LocalDateTime.now().plusMonths(12));
                }
            }
        }
        // 처음 프리미엄을 적용할 경우
        else {
            MemberPremium newMemberPremium = MemberPremium.builder()
                .member(member)
                .expirationAt(LocalDateTime.now().plusMonths(12))
                .isActive(true)
                .isFree(false)
                .build();
            memberPremiumRepository.save(newMemberPremium);
        }
    }

    private void verifyStatusCode(int statusCode) {
        switch (statusCode) {
            case 21000:
                System.out.println("[Status code: " + statusCode
                    + "] The request to the App Store was not made using the HTTP POST request method.");
                break;
            case 21001:
                System.out.println("[Status code: " + statusCode
                    + "] This status code is no longer sent by the App Store.");
                break;
            case 21002:
                System.out.println("[Status code: " + statusCode
                    + "] The data in the receipt-data property was malformed or the service experienced a temporary issue. Try again.");
                break;
            case 21003:
                System.out.println(
                    "[Status code: " + statusCode + "] The receipt could not be authenticated.");
                break;
            case 21004:
                System.out.println("[Status code: " + statusCode
                    + "] The shared secret you provided does not match the shared secret on file for your account.");
                break;
            case 21005:
                System.out.println("[Status code: " + statusCode
                    + "] The receipt server was temporarily unable to provide the receipt. Try again.");
                break;
            case 21006:
                System.out.println("[Status code: " + statusCode
                    + "] This receipt is valid but the subscription has expired. When this status code is returned to your server, the receipt data is also decoded and returned as part of the response. Only returned for iOS 6-style transaction receipts for auto-renewable subscriptions.");
                break;
            case 21008:
                System.out.println("[Status code: " + statusCode
                    + "] This receipt is from the production environment, but it was sent to the test environment for verification.");
                break;
            case 21009:
                System.out.println("[Status code: " + statusCode
                    + "] Internal data access error. Try again later.");
                break;
            case 21010:
                System.out.println("[Status code: " + statusCode
                    + "] The user account cannot be found or has been deleted.");
                break;
            default:
                System.out.println("[Status code: " + statusCode
                    + "] The receipt for the App Store is incorrect.");
                break;
        }
        throw new IllegalStateException(
            "[/verifyReceipt] The receipt for the App Store is incorrect.");
    }
}
