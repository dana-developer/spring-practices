package emaillist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import emaillist.repository.EmaillistRepository;
import emaillist.vo.EmaillistVo;

@Controller
public class EmaillistController {
	
	// 개발자가 new를 하지 않고, 서로 연관관계 등 DI(객체에 대한 관리)를 컨테이너가 해준다.
//	@Autowired
	private EmaillistRepository emaillistRepository;
	
	public EmaillistController(EmaillistRepository emaillistRepository) {
		this.emaillistRepository = emaillistRepository;
	}
	
	@RequestMapping("/")
	public String index(Model model) {
		List<EmaillistVo> list = emaillistRepository.findAll();
		model.addAttribute("list", list);
		return "index";
	}
	
	@RequestMapping("/form")
	public String form() {
		return "form";
	}
	
	@RequestMapping("/add")
	public String add(EmaillistVo vo) {
		emaillistRepository.insert(vo);
		return "redirect:/";
	}
}
