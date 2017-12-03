package com.etfbp.dms.controllers;
 
import javax.servlet.http.HttpSession;

import org.apache.coyote.http11.Http11AprProtocol;
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
    
    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.POST)
    public String login(Model model, @RequestParam(value="emailLogin") String email, 
    		@RequestParam(value="passwordLogin") String password, HttpSession session) {
    	User user = userService.findUserByEmail(email);
    	
    	String view = "";
    	
    	if (user != null) {
    		if (password.equals(user.getPassword())) {
    			
       			//user login success
    			//potrebno je spremiti u sesiju podatke
    			session.setAttribute("userid", user.getID());
    			session.setAttribute("username", user.getUserName());
    			session.setAttribute("password", user.getPassword());
    			session.setAttribute("name", user.getName());
    			session.setAttribute("lastname", user.getLastName());
    			session.setAttribute("email", user.getEmail());
    			session.setAttribute("roleid", user.getRoleID());
    			
    			view = "home";
    		}
    		else {
    			model.addAttribute("message", "Pogrešna šifra!");
    			view = "login";
    		}
    	}
    	else {
    		model.addAttribute("message", "Pogrešan email!");
    		view = "login";
    	}
    	
    	return view;
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