package jusosearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Results {
    @JsonProperty("common")
    private Common common;

    @JsonProperty("juso")
    private List<Juso> juso;
}
