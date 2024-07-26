package jusosearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JusoResponse {

    // @JsonProperty : Java 객체의 필드와 JSON 키 간의 매핑 지정
    @JsonProperty("results")
    private Results results;
}
