package jusosearch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Common {
    private String totalCount;
    private int currentPage;
    private int countPerPage;
    private String errorCode;
    private String errorMessage;
}
