package jusosearch.controller;

import jusosearch.config.auth.PrincipalDetails;
import jusosearch.dto.JusoResponse;
import jusosearch.dto.JusoSearch;
import jusosearch.dto.SearchHistory;
import jusosearch.dto.SearchResult;
import jusosearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class JusoController {

    private final SearchService searchService;

    /**
     * 공공 도로명 주소 API 호출
     */
    @PostMapping("/juso")
    public JusoResponse getJuso(@RequestBody JusoSearch jusoSearch) throws IOException {

        int countPerPage = 5;

        // API 호출 URL 정보 설정
        String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do?" +
                "currentPage="+jusoSearch.getCurrentPage()+
                "&countPerPage="+countPerPage+
                "&keyword=" + jusoSearch.getKeyword() +
//                "&confmKey=devU01TX0FVVEgyMDI0MDMxMDE0MjAwMDExNDU4MTI=" +
                "&confmKey=U01TX0FVVEgyMDI0MDcyNTE4NTU0NzExNDk2MzI=" +
                "&resultType=json";

        log.info("URL = " + apiUrl);
        log.info("jusoSearch = keyword:[{}], currentPage:[{}]", jusoSearch.getKeyword(), jusoSearch.getCurrentPage());

        // RestTemplate.getForObject() : get 요청을 받아 객체 타입으로 반환
        RestTemplate restTemplate = new RestTemplate();
        String jusoStr = restTemplate.getForObject(apiUrl, String.class);
        JusoResponse jusoResponse = restTemplate.getForObject(apiUrl, JusoResponse.class);

        log.info("jusoStr = {}", jusoStr);
        log.info("jusoResponse = {}", jusoResponse);

        return jusoResponse;
    }

    /**
     * 검색 이력 저장
     *  - 저장 후, 조회된 결과를 클라이언트에 반환
     */
    @PostMapping("/address")
    public ResponseEntity<String> saveAddress(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @RequestBody String address){

        String id = principalDetails.getUsername();
        log.info("로그인 아이디 = {}, 검색한 주소 = {}", id, address);

        // 검색 이력 저장
        searchService.saveAddr(id, address);

        // 저장 후 조회된 결과를 클라이언트에 반환
        return ResponseEntity.ok().body(id + ": 주소 저장 완료");
    }

    /**
     * 주소 이력 조회
     */
    @GetMapping("/address/{currentPage}")
    @ResponseBody
    public ResponseEntity<SearchResult> findAddress(@PathVariable int currentPage , @AuthenticationPrincipal PrincipalDetails principalDetails) {

        String id = principalDetails.getUsername();

        log.info("id = {}, currentPage = {}", id, currentPage);

        //limit 구하기
        //페이지 -> (pageNumber - 1) * countPerpage
        // 4 -> 3 * 5 -> limit 15,5

        int index = (currentPage - 1) * 5;
        List<SearchHistory> historyList = searchService.searchList(id, index);
        int totalPages = searchService.findTotalPages(id);
        SearchResult searchResult = new SearchResult(historyList, totalPages);
        log.info("searchResult = {}", searchResult);

        return ResponseEntity.ok().body(searchResult);
    }

    /**
     * 검색 이력 삭제
     *  - 검색 일시를 기준으로 삭제
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteAddr(String srchTime) {
        log.info("삭제할 일시 = {}", srchTime);

        searchService.delete(srchTime);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * 검색 이력 전체 삭제
     */
    @PostMapping("/deleteAll")
    public ResponseEntity<String> deleteAllSearchHistory() {
        log.info("전체 삭제");
        searchService.deleteAll();

        return ResponseEntity.ok("전체 검색 이력 삭제 완료");
    }

}
