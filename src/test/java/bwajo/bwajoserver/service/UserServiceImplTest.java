package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Role;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // 가짜 객체(Mock) 생성

    @InjectMocks
    private UserServiceImpl userService; // UserServiceImpl에 Mock 주입

    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole(Role.USER);
    }

    @Test
    void 회원가입_성공() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResultMessage result = userService.register(testUser);

        assertEquals(201, result.getCode());
        assertEquals("회원가입 성공", result.getMessage());

        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void 로그인_성공() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.login("test@example.com", "password123");

        assertEquals(200, result.getCode());
        assertEquals("로그인 성공", result.getMessage());
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.login("test@example.com", "wrongPassword");

        assertEquals(401, result.getCode());
        assertEquals("로그인 실패", result.getMessage());
    }

    @Test
    void 비밀번호_찾기_성공() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        String password = userService.findPassword("test@example.com");

        assertEquals("password123", password);
    }

    @Test
    void 비밀번호_찾기_실패_없는_이메일() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

        String password = userService.findPassword("unknown@example.com");

        assertEquals("해당 이메일 없음", password);
    }

    @Test
    void 비밀번호_변경_성공() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.updatePassword("test@example.com", "password123", "newPassword");

        assertEquals(201, result.getCode());
        assertEquals("비밀번호 변경 성공", result.getMessage());

        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void 비밀번호_변경_실패_잘못된_기존_비밀번호() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.updatePassword("test@example.com", "wrongPassword", "newPassword");

        assertEquals(401, result.getCode());
        assertEquals("비밀번호 변경 실패", result.getMessage());
    }

    @Test
    void 회원_삭제_성공() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.delete("test@example.com", "password123");

        assertEquals(200, result.getCode());
        assertEquals("회원 삭제 성공", result.getMessage());

        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void 회원_삭제_실패_잘못된_비밀번호() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        ResultMessage result = userService.delete("test@example.com", "wrongPassword");

        assertEquals(401, result.getCode());
        assertEquals("회원 삭제 실패", result.getMessage());
    }
}