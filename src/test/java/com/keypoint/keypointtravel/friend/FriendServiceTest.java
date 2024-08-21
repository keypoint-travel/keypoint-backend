package com.keypoint.keypointtravel.friend;

import com.keypoint.keypointtravel.friend.dto.DeleteUseCase;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.friend.service.FriendService;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendService friendService;

    @Test
    public void deleteFriendTest() {
        // given : 해당하는 친구 관계가 존재하지 않을 시 예외 처리
        when(friendRepository.updateIsDeletedById(any(), any(), anyBoolean())).thenReturn(0L);

        //when & then
        assertThatThrownBy(() -> friendService.deleteFriend(new DeleteUseCase(1L, 2L)))
            .isInstanceOf(GeneralException.class);
    }
}
