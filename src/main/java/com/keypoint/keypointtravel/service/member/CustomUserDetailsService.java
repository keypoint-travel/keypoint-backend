package com.keypoint.keypointtravel.service.member;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.entity.member.Member;
import com.keypoint.keypointtravel.repository.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(email);
        if (members.isEmpty()) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL);
        }

        return CustomUserDetails.toCustomUserDetails(members.get(0));
    }
}
