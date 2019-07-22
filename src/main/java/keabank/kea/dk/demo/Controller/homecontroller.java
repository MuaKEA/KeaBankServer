package keabank.kea.dk.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homecontroller {






@GetMapping("/index")
    public String index() {
      return "index";

}}
