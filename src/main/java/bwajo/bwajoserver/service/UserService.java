package bwajo.bwajoserver.service;

import bwajo.bwajoserver.entity.User;

public interface UserService {
    // 회원가입
    String register(User user);

    // 로그인
    String login(String email, String password);

    // 비밀번호 찾기
    String findPassword(String email);

    // 비밀번호 변경
    String updatePassword(String email, String password, String newPassword);

    // 계정 삭제
    String delete(String email, String password);

}
