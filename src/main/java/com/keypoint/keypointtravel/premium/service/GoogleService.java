package com.keypoint.keypointtravel.premium.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.PremiumErrorCode;
import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.global.config.GoogleCredentialsConfig;
import com.keypoint.keypointtravel.premium.dto.useCase.GooglePurchaseUseCase;
import com.keypoint.keypointtravel.premium.entity.GooglePurchaseHistory;
import com.keypoint.keypointtravel.premium.repository.GooglePurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final GoogleCredentialsConfig googleCredentialsConfig;

    private final MemberRepository memberRepository;

    private final GooglePurchaseHistoryRepository googlePurchaseHistoryRepository;

    private final PremiumService premiumService;

    /**
     * 소모품 (Consumable) 구매 영수증 검증 및 기록 업데이트, 프리미엄 적용 함수
     *
     * @Param memberId, amount, currency, 결제 정보 useCase
     */
    @Transactional
    public void updatePurchaseHistory(GooglePurchaseUseCase useCase) throws GeneralSecurityException, IOException {
        // 1. 구글 영수증 검증
        ProductPurchase purchase = googleInAppPurchaseVerify(useCase);
        // 2. 구글 앱 스토어 결제 성공 시, 결제 내역 저장
        updateHistory(useCase, purchase);
        // 3. 회원 프리미엄 적용
        premiumService.updateMemberPremium(useCase.getMemberId());
    }

    private ProductPurchase googleInAppPurchaseVerify(GooglePurchaseUseCase useCase) throws GeneralSecurityException, IOException {

        AndroidPublisher publisher = googleCredentialsConfig.androidPublisher();

        AndroidPublisher.Purchases.Products.Get get = publisher.purchases().products().
            get(useCase.getPackageName(), useCase.getProductId(), useCase.getPurchaseToken());
        // ProductPurchase 객체 세부 내용은 https://developers.google.com/android-publisher/api-ref/rest/v3/purchases.products?hl=ko#resource:-productpurchase 참고
        ProductPurchase purchase = get.execute();

        // 0이면 결제완료 1이면 취소된 주문건, 2면 결제 보류중인 주문건
        // 검증하는데 취소된 결제건인 경우
        if (purchase.getPurchaseState() != 0) {
            throw new GeneralException(PremiumErrorCode.INCORRECT_RECEIPT);
        }
        return purchase;
    }

    private void updateHistory(GooglePurchaseUseCase useCase, ProductPurchase purchase) {
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        GooglePurchaseHistory history = GooglePurchaseHistory.builder()
            .member(member)
            .orderId(purchase.getOrderId())
            .productId(useCase.getProductId())
            .purchaseToken(useCase.getPurchaseToken())
            .amount(useCase.getAmount())
            .currency(useCase.getCurrency())
            .purchasedAt(new Date(purchase.getPurchaseTimeMillis()))
            .purchaseStatus(PurchaseStatus.COMPLETED)
            .build();
        try {
            googlePurchaseHistoryRepository.save(history);
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
    }


}
