package jusosearch.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private String id;
    private String password;
    private String name;
    private String email;
    private String provider;
    private String providerId;
    private String createdAt;
    private String updatedAt;
    private String loadAt;
}
