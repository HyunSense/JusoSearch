package jusosearch.controller;

import jusosearch.service.UserService;
import jusosearch.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 로직 처리
     */
    @PostMapping("/join")
    public String join(User user) {

        log.info("insertUser name = {}", user.getName());
        userService.save(user);
        log.info("회원가입 성공 {}", user.getId());

        return "redirect:/login";   // 가입 후 로그인 페이지로 이동
    }

    /**
     * 중복 아이디 체크
     * - Ajax 호출
     */
    @ResponseBody
    @PostMapping("/checkId")
    public int checkOneId(@RequestBody String memberId){
        log.info("중복 체크할 아이디 = {}", memberId);
        System.out.println(memberId);
        return userService.checkOneId(memberId);
    }
}
