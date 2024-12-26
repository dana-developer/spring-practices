package hellospring.controller;

import java.io.IOException;
import java.io.Writer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MyAnnotation
@Controller
public class HelloController {
	
	@RequestMapping("/hello")
	public String hello() {
		return "/WEB-INF/views/hello.jsp";
	}
	
	@RequestMapping("/hello2")
	public String hello2(@RequestParam("name") String name) {
		System.out.println("name : "+name);
		return "/WEB-INF/views/hello.jsp";
	}
	
	@RequestMapping("/hello3")
	public ModelAndView hello3(@RequestParam("name") String name) {
		// 추천하지 않는 방식(new로 반환하고, 코드가 길어서)
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/WEB-INF/views/hello3.jsp");
		mav.addObject("name", name);
		
		return mav;
	}
	
	@RequestMapping("/hello4")
	public String hello4(@RequestParam("name") String name, Model model) {
		model.addAttribute("name", name); // dispatcher servlet에서 model 생성해준다.
		return "/WEB-INF/views/hello3.jsp";
	}
	
	@ResponseBody // @RestController를 붙이면, 해당 어노테이션을 작성하지 않아도 된다.
	@RequestMapping("/hello5")
	public String hello5() {
		return "<h1>Hello Spring</h1>";
	}
	
	@RequestMapping("/hello6")
	public String hello6() {
		return "redirect:hello";
	}
	
	@RequestMapping("/hello7")
	public void hello7(
			HttpServletRequest req,
			HttpServletResponse res,
			Writer out) throws IOException {
		// spring 원칙을 역행하는 것이므로 권장X
		String name = req.getParameter("name");
		System.out.println("name : "+name);
//		res.getWriter().print("<h1>hello World</h1>");
		out.write("<h1>hello World</h1>");
	}
}
