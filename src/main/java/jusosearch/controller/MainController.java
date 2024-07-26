package jusosearch.controller;

import jusosearch.config.auth.PrincipalDetails;
import jusosearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * GET 방식, 화면 조회
 */

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final SearchService searchService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value="error", required = false) String error, Model model){
        log.info("로그인 실패 시 error = {}", error);
        model.addAttribute("error", error);
//        model.addAttribute("exception", exception);

        return "login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "join";
    }

    @GetMapping("/map")
    public String showMap(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        // 페이지 기본값
        int index = 0; // LIMIT 0,5
        String id = principalDetails.getUser().getId();

        // 상단 바에 사용자 이름 출력
        String name = principalDetails.getUser().getName();
        log.info("name = {}", name);
        model.addAttribute("name", name);

        return "map";
    }
}
