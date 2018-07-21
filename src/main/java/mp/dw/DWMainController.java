package mp.dw;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DWMainController {
	
	@GetMapping(value = "/")
	public String mainPage() {
		return "index.html";
	}
	
	@GetMapping(value = "/login")
	public String loginPage() {
		return "loginPage.html";
	}
}
