package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import com.keypoint.keypointtravel.notification.dto.dto.CommonFCMTokenDTO;
import com.keypoint.keypointtravel.notification.dto.useCase.FCMTokenUseCase;
import com.keypoint.keypointtravel.notification.entity.FCMToken;
import com.keypoint.keypointtravel.notification.repository.FCMTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMTokenService {

    private final FCMTokenRepository fcmTokenRepository;
    private final ReadMemberService readMemberService;

    /**
     * FCM token 등록하는 함수
     *
     * @param useCase
     */
    @Transactional
    public void addFCMToken(FCMTokenUseCase useCase) {
        Long memberId = useCase.getMemberId();
        Member member = readMemberService.findMemberById(memberId);
        String token = useCase.getFcmToken();

        // 1. 요청 온 토큰이 DB 등록되어 있는지 확인
        Optional<CommonFCMTokenDTO> tokenOptional = fcmTokenRepository.findByToken(token);

        if (tokenOptional.isPresent()) {
            CommonFCMTokenDTO dto = tokenOptional.get();

            // 1-1. memberId가 다른 경우, 아이디만 업데이트
            if (!dto.getMemberId().equals(memberId)) {
                fcmTokenRepository.updateMember(dto.getId(), member);
            }

        } else {
            // 1-2. 새로운 토큰으로 저장
            FCMToken fcmToken = FCMToken.of(token, member);
            fcmTokenRepository.save(fcmToken);
        }
    }
}
