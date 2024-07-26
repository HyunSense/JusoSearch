package jusosearch.config.oauth;

import jusosearch.config.auth.PrincipalDetails;
import jusosearch.config.oauth.provider.KakaoUserInfo;
import jusosearch.config.oauth.provider.Oauth2UserInfo;
import jusosearch.dto.User;
import jusosearch.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 카카오부터 받은 userRequest 데이터에 대한 후처리 함수
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    // 카카오부터 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest.getClientRegistration().getClientId = {}", userRequest.getClientRegistration().getRegistrationId());
        log.info("userRequest.getAccessToken() = {}", userRequest.getAccessToken());
        log.info("userRequest.getClientRegistration() = {}", userRequest.getClientRegistration());
        log.info("OAuth2User = {}", super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals("kakao")) {
            log.info("카카오 로그인 요청");
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("잘못된 Oauth 요청");
        }


        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String loginId = provider + "_" + providerId;
        log.info("loginId = {}", loginId);
        String password = bCryptPasswordEncoder.encode("필요없음");
        String name = oauth2UserInfo.getName();
        log.info("loginId = {}", name);
        String email = oauth2UserInfo.getEmail();
        log.info("loginId = {}", email);

        Optional<User> userOpt = userMapper.findUserByUserName(loginId);

        User user = null;
        if (userOpt.isEmpty()) {
            log.info("회원가입 최초");
            user = User.builder()
                    .id(loginId)
                    .password(password)
                    .name(name)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userMapper.save(user);
        } else {
            user = userOpt.get();
            log.info("이미 등록되어있는 회원입니다= {}", user);

        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
