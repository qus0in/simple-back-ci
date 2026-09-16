package org.example.simpleback.app;

import org.example.simpleback.domain.model.User;
import org.example.simpleback.domain.port.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceDefaultUserTest {
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("신규 요구사항: 기본 사용자 이름 검증 (의도적 실패)")
    void checkDefaultUserName_Failure() {
        given(userRepository.count()).willReturn(0L);
        given(userRepository.findAll()).willReturn(List.of());
        UserService userService = new UserService(userRepository);

        userService.getUsers();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        // 실제 저장값은 "infra-admin"이지만 "vip-admin"으로 잘못 검증하여 실패 유도
        assertThat(userCaptor.getValue().name()).isEqualTo("vip-admin");
    }
}
