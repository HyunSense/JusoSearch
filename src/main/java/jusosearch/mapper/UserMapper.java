package jusosearch.mapper;

import jusosearch.dto.SearchHistory;
import jusosearch.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    // 회원 가입 로직
    void save(User user);


    // 아이디로 사용자 정보 조회
    User findByUserName(String id);


    // 검색 이력 저장
    void saveAddr(String id, String address);


    // 검색 이력 조회
    List<SearchHistory> findAddressHistoryById(String id, int index);


    // 검색 이력 삭제
    void delete(String srchTime);


    // 검색 이력 전체 삭제
    void deleteAll();


    // 토탈 페이지를 구하기 위한 검색 이력 전체 수 조회
    int countAllById(String id);


    // 사용자 아이디 중복 체크
    int selectOneById(String id);

    // OAuth2 추가
    Optional<User> findUserByUserName(String userName);

}
