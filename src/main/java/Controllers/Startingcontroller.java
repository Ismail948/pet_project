package Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Startingcontroller {

	
	@GetMapping("/")
	public String indexpage() {
		System.out.println("home page");
		return "index";
	}
	
	@GetMapping("/explore")
    public String explorepage() {
        return "explore";
    }
}
