package jusosearch.service;

import jusosearch.dto.SearchHistory;
import jusosearch.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserMapper userMapper;

    /**
     * 검색 이력 저장
     */
    public void saveAddr(String id, String address) {
        userMapper.saveAddr(id, address);
    }

    /**
     *  주소 이력 조회
     */
    public List<SearchHistory> searchList(String id, int index) {
        return userMapper.findAddressHistoryById(id, index);
    }

    /**
     * 검색 이력 테이블 페이징 관련
     * - 토탈 페이지를 구하기 위한 검색 이력 전체 수 조회
     */
    public int countAllById(String id) {

        return userMapper.countAllById(id);
    }

    /**
     * 검색 이력 테이블 페이징 관련
     * - 토탈 페이지
     */
    public int findTotalPages(String id) {

        int countAll = countAllById(id);
        int countPerPage = 5;
        // 전체 페이지 = 전체 카운트 / 페이지당 개수
        int totalPages = (int) Math.ceil((float) countAll / countPerPage);
        return totalPages;
    }

    /**
     * 검색 이력 삭제
     *  - 검색 일시를 기준으로 삭제
     */
    public void delete(String srchTime){
        userMapper.delete(srchTime);
    }

    /**
     * 검색 이력 전체 삭제
     */
    public void deleteAll() {
        userMapper.deleteAll();
    }
}
