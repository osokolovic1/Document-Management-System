package com.etfbp.dms.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.etfbp.dms.models.Document;
import com.etfbp.dms.models.DocumentInfo;
import com.etfbp.dms.models.FileBucket;
import com.etfbp.dms.models.Grupa;
import com.etfbp.dms.models.User;
import com.etfbp.dms.models.UserInfo;
import com.etfbp.dms.services.DocumentService;
import com.etfbp.dms.services.GroupService;
import com.etfbp.dms.services.UserService;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    
    @RequestMapping(value = "/user/info/all", method = RequestMethod.GET) 
    public List<UserInfo> userInfo() {
    	List<UserInfo> usersInfo = new ArrayList<>();
    	Set<User> users = userService.findAll();
    	Iterator<User> iterator = users.iterator();
    	while(iterator.hasNext()) {
    		User element = iterator.next();
    		usersInfo.add(new UserInfo(element.getID(), element.getName(), element.getLastName(), element.getEmail()));
    	}
    	return usersInfo;
    }
    
    @RequestMapping(value= "/documents/view/{docId}", method=RequestMethod.GET)
    public String viewDocument (@PathVariable String docId, HttpServletResponse response, HttpSession session) throws IOException{
        /*if(session.getAttribute("userid") == null)
        	return "Forbidden";*/
        int userId=2;
    	Document document = documentService.findById(Integer.parseInt(docId));
    	if(document == null)
    		return "Not found";
    	
    	
    	Boolean imaPravoPristupa = false;
        Iterator<User> iterator = document.getUsers().iterator();
        while (iterator.hasNext()) {
            User element = iterator.next();
            if(element.getID().equals(userId)) {
            	imaPravoPristupa = true;
            	continue;
            }
        }
    	
        /*if(!imaPravoPristupa)
        	return "Forbidden";*/
        
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
    
    //users documents
    @RequestMapping(value="/documents/my-documents", method = RequestMethod.GET)
    public List<DocumentInfo> getMyDocuments(HttpSession session) {   	
    	int userId;// = (int)session.getAttribute("userid");
    	userId = 2;
    	
    	
    	List<DocumentInfo> documentInfo = new ArrayList<>();
    	List<Document> docs = documentService.findAllByUserPermission(userId);
    	for(Iterator<Document> it = docs.iterator(); it.hasNext(); ) {  
    		Document d = it.next();
    		documentInfo.add(new DocumentInfo(d.getId(), d.getName(), d.getDescription(), d.getType()));
    	}
    	
    	return documentInfo;
    	
    }
    //documents shared with user
    @RequestMapping(value = { "/documents/shared" }, method = RequestMethod.GET)
    public List<DocumentInfo> getSharedDocuments(HttpServletRequest request, HttpServletResponse response) {

    	HttpSession session = request.getSession(true);
    	
    	int userId;// = (int)session.getAttribute("userid");
    	userId = 2;
    	List<DocumentInfo> documentInfo = new ArrayList<>();
    	
        List<Document> docs = documentService.findAllByUserId(userId);
        for(Iterator<Document> it = docs.iterator(); it.hasNext();) {
        	Document d = it.next();
        	documentInfo.add(new DocumentInfo(d.getId(), d.getName(), d.getDescription(), d.getType()));
        }
        return documentInfo;
    }
    
    private void saveDocument(FileBucket fileBucket, User user, String fileDescription, Set<User> sharedWithUsers,
    		Set<Grupa> sharedWithGroups) throws IOException{    
        MultipartFile multipartFile = fileBucket.getFile();
        Document document = new Document(user.getID(),
        							multipartFile.getOriginalFilename(),
        							fileDescription,
        							multipartFile.getContentType(),
        							multipartFile.getBytes(), 
        							sharedWithUsers, 
        							sharedWithGroups);
        documentService.saveDocument(document);
        System.out.println(document.getId());
    }
    
    @RequestMapping(value = { "/documents/add" }, method = RequestMethod.POST)
    public String addDocuments(@RequestParam("file") MultipartFile file, @Valid FileBucket fileBucket, BindingResult result, ModelMap model, 
    		 HttpSession session, HttpServletRequest request) throws IOException{
    	
    	
    	int userId;// = (int)session.getAttribute("userid");
    	userId = 2;
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	
        if (result.hasErrors()) {
            System.out.println("validation errors");
 
           // List<Document> documents = documentService.findAll();
            Set<Document> docs = userService.findAllUserDocumentsById(user.getID().intValue());
            model.addAttribute("documents", docs);
             
            return "managedocuments";
        } else {
             
            System.out.println("Fetching file");
            
            model.addAttribute("user", user);
            
            //otherUsers
            //sharedGroups
            String[] users = request.getParameterValues("otherUsers");
            String[] groups = request.getParameterValues("sharedGroups");

            Set<User> sharedWithUsers = new HashSet<User>();
            Set<Grupa> sharedWithGroups = new HashSet<Grupa>();
            /*Ovo ce trebati optimizirati*/
            
            if(users != null)
            	for (String u : users) 
            		sharedWithUsers.add(userService.findByUserName(u));
            else
            	sharedWithUsers = null;
            
            if(groups != null)
            	for (String g : groups)
            		sharedWithGroups.add(groupService.findByGroupName(g));
            else
            	sharedWithGroups = null;
           
            model.addAttribute("user", user);
            fileBucket.setFile(file);

            saveDocument(fileBucket, user, "Def desc.", sharedWithUsers, sharedWithGroups);
 
            return "OK";
        }
    }
}
