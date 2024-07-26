package jusosearch.service;

import jusosearch.dto.User;
import jusosearch.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 회원가입 로직
     *  - 비밀번호 단방향 암호화
     */
    public void save(User user){
        // 비밀번호 단방향 암호화
        String rawPassword = user.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setPassword(encodedPassword);

        userMapper.save(user);
    }


    /**
     * 아이디 중복 체크
     */
    public int checkOneId(String id) {
        int result = userMapper.selectOneById(id);

        return result;
    }
}
