package jusosearch.config.auth;

import jusosearch.dto.User;
import jusosearch.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        log.info("username = {}", id);
        User user = userMapper.findByUserName(id);

        if (user != null) {
            log.info("null이 아닙니다.");
            return new PrincipalDetails(user);
        }
        log.info("null 입니다.");
        return null;
    }
}
