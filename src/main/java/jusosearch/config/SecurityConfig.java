package jusosearch.config;

import jusosearch.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터 체인에 등록(스프링 시큐리티 활성화)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, PreAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFailureHandler customFailureHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()                                             // 로컬에서 확인을 위한 csrf 활성화
                .authorizeRequests()
                .antMatchers("/login", "/join", "/", "/checkId").permitAll()    // 해당 url : 모두에게 권한 허용
                .anyRequest().authenticated()                             // 정해진 url을 제외한 어떠한 요청이든 인증 필요
                .and()
                .formLogin()
                .loginPage("/login")                                      // 로그인 페이지 지정
                .usernameParameter("id")
                .loginProcessingUrl("/login")
//                .failureHandler(customFailureHandler)                   // 로그인 실패 시 처리하는 핸들러
                .defaultSuccessUrl("/map", true) // 로그인 성공 시, /map으로 이동
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")                                    // 로그아웃 성공 후 리다이렉트할 URL 설정
//                .invalidateHttpSession(true) // HTTP 세션 무효화
//                .deleteCookies("JSESSIONID") // 로그아웃 시 쿠키 삭제
                .permitAll()    // 접근 허용
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/map", true)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }

    // Spring Security를 적용하지 않을 리소스 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
                // 기본적으로 제공되는 정적 리소스 경로 무시
    }

}
