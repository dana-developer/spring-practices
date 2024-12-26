package hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @RequestMapping 메소드 단독 매핑
 */

@Controller
public class BoardController {
	
	@ResponseBody
	@RequestMapping("/board/write")
	public String write() {
		return "BoardController:write()";
	}
	
	@ResponseBody
	@RequestMapping("/board/view/{no}") // 파라미터가 아닌 path로 입력받는 경우
	public String view(@PathVariable("no") Long number) {
		return "BoardController:view("+number+")";
	}
}
