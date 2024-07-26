package jusosearch.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo {

    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccountAttributes;
    private Map<String, Object> profileAttributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) kakaoAccountAttributes.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

    @Override
    public String getName() {
        return profileAttributes.get("nickname").toString();
    }
}
