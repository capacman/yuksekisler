package com.yuksekisler.interfaces.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTTPErrorController {

    @RequestMapping(value="/errors/404.html")
    public String handle404() {
        return "defaultView";
    }
    
    @RequestMapping(value="/index.html")
    public String handleIndex() {
        return "defaultView";
    }
    
    @RequestMapping(value="/login.html")
    public String handleLogin() {
        return "loginView";
    }

}
