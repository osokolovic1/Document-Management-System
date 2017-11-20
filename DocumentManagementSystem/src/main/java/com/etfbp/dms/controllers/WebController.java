package com.etfbp.dms.controllers;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.etfbp.dms.models.User;
import com.etfbp.dms.repo.UserRepository;
import com.etfbp.dms.services.UserService;
 
 
 
@Controller
public class WebController {
 
	@Autowired
	UserService userService;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping(value= {"/login", "/"}, method=RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    
    @RequestMapping(value= "/registration", method=RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }
 
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registrationSubmit(@ModelAttribute User user, Model model) {
    	
        model.addAttribute("user", user);
        String info = String.format("Customer Submission: id = %d, firstname = %s, lastname = %s", 
        								user.getID(), user.getName(), user.getLastName());
        log.info(info);
        
        userService.registerUser(user);
                
        return "login";
    }
    
    
}