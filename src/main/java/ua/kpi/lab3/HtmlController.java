package ua.kpi.lab3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class HtmlController {

    @Autowired private CaptchaProperties captchaProperties;
    @Autowired private CaptchaService captchaService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("time", LocalDateTime.now().withNano(0));
        return "index";
    }

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public ResponseEntity<String> sitemap() {
        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                <url><loc>https://yevhenii-test.ddns.net/</loc></url>
                <url><loc>https://yevhenii-test.ddns.net/indexed</loc></url>
            </urlset>
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }

    @GetMapping("indexed")
    public String indexed() {
        return "indexed";
    }

    @GetMapping("/secure")
    public String secure(HttpSession session, Model model) {
        Boolean verified = (Boolean) session.getAttribute("captcha_verified");
        if (Boolean.TRUE.equals(verified)) {
            return "secure-content"; // shows protected content
        } else {
            model.addAttribute("siteKey", captchaProperties.siteKey());
            return "secure-captcha"; // shows captcha form
        }
    }

    @PostMapping("/secure")
    public String handleCaptcha(@RequestParam("g-recaptcha-response") String response,
                                HttpServletRequest request,
                                HttpSession session,
                                Model model) {
        boolean verified = captchaService.verify(response, request.getRemoteAddr());
        if (verified) {
            session.setAttribute("captcha_verified", true);
            return "redirect:/secure";
        } else {
            model.addAttribute("siteKey", captchaProperties.siteKey());
            model.addAttribute("error", "CAPTCHA failed");
            return "secure-captcha";
        }
    }
}
