package jusosearch.config.auth;

import jusosearch.dto.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
//Security Session -> Authentication -> UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // OAuth2User 오버라이딩 ----------
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override // attributes
    public String getName() {
        return (String) attributes.get("sub");
    }


    // UserDetails 오버라이딩 ----------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 유저의 권한(user, admin 등) 설정이 없다면, null
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    // 계정 만료 여부 ( true : 만료 안됨 )
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    // 계정 잠김 여부 ( true : 잠기지 않음 )
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 비밀번호 만료 여부 ( true : 만료 안됨 )
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 계정 활성화 여부 ( true : 활성화 )
    public boolean isEnabled() {
        return true;
    }
}
