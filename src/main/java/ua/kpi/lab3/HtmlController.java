package ua.kpi.lab3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class HtmlController {

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("time", LocalDateTime.now().withNano(0));
        return "index";
    }

    @GetMapping("/secure")
    public String secure() {
        return "secure";
    }
}
