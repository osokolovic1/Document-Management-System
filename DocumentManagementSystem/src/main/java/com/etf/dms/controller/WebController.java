package com.etf.dms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	
	@RequestMapping(value={"/", "home"})
    public String home(){
        return "home";
    }
   
    @RequestMapping(value={"/login"})
    public String login(){
        return "login";
    }

}
