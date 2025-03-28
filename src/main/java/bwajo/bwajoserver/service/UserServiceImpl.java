package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResultMessage register(User user) {
        userRepository.save(user);  // 사용자 저장
        return new ResultMessage(201, "회원가입 성공");
    }

    @Override
    public ResultMessage login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return new ResultMessage(200, "로그인 성공");
        }
        return new ResultMessage(401, "로그인 실패");
    }

    @Override
    public String findPassword(String email) {
        User user = userRepository.findByEmail(email);
        return (user != null) ? user.getPassword() : "해당 이메일 없음";
    }

    @Override
    public ResultMessage updatePassword(String email, String password, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            user.setPassword(newPassword);
            userRepository.save(user);  // 업데이트 저장
            return new ResultMessage(201, "비밀번호 변경 성공");
        }
        return new ResultMessage(401, "비밀번호 변경 실패");
    }

    @Override
    public ResultMessage delete(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            userRepository.delete(user);
            return new ResultMessage(200,"회원 삭제 성공");
        }
        return new ResultMessage(401,"회원 삭제 실패");
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
