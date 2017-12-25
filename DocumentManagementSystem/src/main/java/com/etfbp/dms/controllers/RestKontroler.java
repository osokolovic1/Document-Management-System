package com.etfbp.dms.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfbp.dms.models.Document;
import com.etfbp.dms.models.FileBucket;
import com.etfbp.dms.models.User;
import com.etfbp.dms.services.DocumentService;
import com.etfbp.dms.services.GroupService;
import com.etfbp.dms.services.UserService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Transactional
public class RestKontroler {	
	@Autowired
	UserService userService;
	
	@Autowired
	DocumentService documentService;
	
	@Autowired
	GroupService groupService;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam(value="username") String username, 
    		@RequestParam(value="password") String password, HttpSession session) {
    	
    	User user = userService.findUserByEmail(username);
    	
    	if(user == null) 
    		user = userService.findByUserName(username);
    	
    	if(user == null)
    		return "Login error!";
    	
    	if (!password.equals(user.getPassword())) {		
    		return "Login error!";
    	}

			//user login success
		//potrebno je spremiti u sesiju podatke
		session.setAttribute("userid", user.getID());
		session.setAttribute("username", user.getUserName());
		session.setAttribute("name", user.getName());
		session.setAttribute("lastname", user.getLastName());
		session.setAttribute("email", user.getEmail());
		session.setAttribute("roleid", user.getRole().getRole());
    	
    	return "OK";
    }
    
    @RequestMapping(value="/user/registration", method=RequestMethod.POST)
    public String registrationSubmit(@ModelAttribute User user, Model model, HttpSession session) {
        if(session.getAttribute("userid") != null)
        	return "Error";
        
        model.addAttribute("user", user);
        
        User existsEmail = userService.findUserByEmail(user.getEmail());
        User existsUserName = userService.findByUserName(user.getUserName());
        String message = "";
        
        if (existsEmail == null && existsUserName == null) {
        	message = "";
            userService.registerUser(user);          
        }
        else {
        	if (existsEmail != null)
        		message += "Korisnik sa unesenom email adresom već postoji!\n";
        	if (existsUserName != null) 
        		message += "Korisnik sa unesenim korisničkim imenom već postoji!\n";
        	
        }
        
        
        return message;
    }
    
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public User userInfo(@RequestParam(value="username") String username) {
    	User user = userService.findByUserName(username);
    	return user;
    }
    
    @RequestMapping(value= "/documents/view/{docId}", method=RequestMethod.GET)
    public String viewDocument (@PathVariable String docId, HttpServletResponse response, HttpSession session) throws IOException{
        if(session.getAttribute("userid") == null)
        	return "Forbidden";
        
    	Document document = documentService.findById(Integer.parseInt(docId));
    	if(document == null)
    		return "Not found";
    	
    	Boolean imaPravoPristupa = false;
        Iterator<User> iterator = document.getUsers().iterator();
        while (iterator.hasNext()) {
            User element = iterator.next();
            if(element.getID().equals((int)session.getAttribute("userid"))) {
            	imaPravoPristupa = true;
            	continue;
            }
        }
    	
        if(!imaPravoPristupa)
        	return "Forbidden";
        
    	byte[] documentInBytes = documentService.findById(Integer.parseInt(docId)).getContent(); 
    	response.setDateHeader("Expires", -1);
    	response.setContentType(document.getType());

        
    	if (document.getType().equals("application/pdf")) {        
            response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");        	
    	}
    	
    	response.setContentLength(documentInBytes.length);
        response.getOutputStream().write(documentInBytes);
        return null;
    }
    
    @RequestMapping(value = { "/documents/shared" }, method = RequestMethod.GET)
    public Map<Integer, String> getSharedDocuments(HttpServletRequest request, HttpServletResponse response) {

    	HttpSession session = request.getSession(true);
    	
    	int userId = (int)session.getAttribute("userid");
    	 
 
        Map<Integer, String> listaDokumenata = new HashMap<>();
        List<Document> docs = documentService.findAllByUserPermissionNotOwning(userId);
        for(Iterator<Document> it = docs.iterator(); it.hasNext();) {
        	Document d = it.next();
        	listaDokumenata.put(d.getId(), d.getName());
        }
        return listaDokumenata;
    }
}
