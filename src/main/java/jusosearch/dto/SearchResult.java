package jusosearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SearchResult {

    private List<SearchHistory> historyList;
    private int totalPages;
}
