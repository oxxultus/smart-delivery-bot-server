package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.User;

public interface UserService {
    // 회원가입
    ResultMessage register(User user);

    // 로그인
    ResultMessage login(String email, String password);

    // 비밀번호 찾기
    String findPassword(String email);

    // 비밀번호 변경
    ResultMessage updatePassword(String email, String password, String newPassword);

    // 계정 삭제
    ResultMessage delete(String email, String password);

    // 회원 찾기
    User getUserByEmail(String email);

}
