package com.etfbp.dms.controllers;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.coyote.http11.Http11AprProtocol;
import org.dom4j.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.etfbp.dms.models.Document;
import com.etfbp.dms.models.FileBucket;
import com.etfbp.dms.models.User;
import com.etfbp.dms.repo.UserRepository;
import com.etfbp.dms.services.DocumentService;
import com.etfbp.dms.services.UserService;
 
 
 
@Controller
public class WebController {
 
	@Autowired
	UserService userService;
	
	@Autowired
	DocumentService documentService;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping(value= {"/login", "/"}, method=RequestMethod.GET)
    public String login(Model model, HttpSession session) {
    	if(session.getAttribute("userid") != null) {
    		return "redirect:/my-documents";
    	}
    	
        model.addAttribute("user", new User());
        return "login";
    }
    
    @RequestMapping(value = "/logout", method=RequestMethod.GET)
    public String logout(Model model, HttpSession session) {
    	if(session.getAttribute("userid") != null) {
    		session.invalidate();
    	}
    	return "redirect:/";
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
    			session.setAttribute("roleid", user.getRole().getRole());
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
    public String registration(Model model, HttpSession session) {
    	if(session.getAttribute("userid") != null) {
    		return "redirect:/my-documents";
    	}
    	
        model.addAttribute("user", new User());
        return "registration";
    }
 
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registrationSubmit(@ModelAttribute User user, Model model, HttpSession session) {
        if(session.getAttribute("userid") != null)
        	return "redirect:/";
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
        
        model.addAttribute("message", message);
        
        return "login";
    }
    
    @RequestMapping(value = { "/download-document-{docId}" }, method = RequestMethod.GET)
    public String downloadDocument(@PathVariable String docId, HttpServletResponse response, HttpSession session) throws IOException {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
    	Document document = documentService.findById(Integer.parseInt(docId));
        response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");
  
        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
  
        return "redirect:/add-document-"+ (int)session.getAttribute("userid");
    }
    
    @RequestMapping(value = { "/download-my-document-{docId}" }, method = RequestMethod.GET)
    public String downloadMyDocument(@PathVariable String docId, HttpServletResponse response, HttpSession session) throws IOException {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
        Document document = documentService.findById(Integer.parseInt(docId));
        response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");
  
        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
  
        return "redirect:/my-documents";
    }
    
    
    @RequestMapping(value= "/viewDocument-{docId}", method=RequestMethod.GET)
    public String viewDocument (@PathVariable String docId,Model model, HttpServletResponse response, HttpSession session) throws IOException{
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
    	Document document = documentService.findById(Integer.parseInt(docId));
    	if (document.getType().equals("application/pdf"))
    	{
    		byte[] documentInBytes = documentService.findById(Integer.parseInt(docId)).getContent();         
            response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
            response.setDateHeader("Expires", -1);
            response.setContentType("application/pdf");
            response.setContentLength(documentInBytes.length);
            response.getOutputStream().write(documentInBytes);
    		
    	}
    	else if (document.getType().equals("text/plain")) {
    		byte[] documentInBytes = documentService.findById(Integer.parseInt(docId)).getContent(); 
            response.setDateHeader("Expires", -1);
            response.setContentType("text/plain");
            response.setContentLength(documentInBytes.length);
            response.getOutputStream().write(documentInBytes);
    	}
    	
        return null;
    }
    @RequestMapping(value = { "/delete-my-document-{docId}" }, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable String docId, HttpSession session) {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
        documentService.deleteById(Integer.parseInt(docId));
        return "redirect:/my-documents";
    }
    
    @RequestMapping(value = { "/my-documents" }, method = RequestMethod.GET)
    public String myDocuments(ModelMap model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	 
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
 
        List<Document> documents = documentService.findAllByUserId(userId);
        model.addAttribute("documents", documents);
        
        return "myDocuments";
    }
    
    @RequestMapping(value = { "/add-document" }, method = RequestMethod.GET)
    public String addDocuments(ModelMap model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	 
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
 
        List<Document> documents = documentService.findAll();
        model.addAttribute("documents", documents);
        System.out.print(documents.get(0).getId());
        return "managedocuments";
    }
    
    @RequestMapping(value = { "/add-document" }, method = RequestMethod.POST)
    public String uploadDocument(@Valid FileBucket fileBucket, BindingResult result, ModelMap model, @RequestParam("file") MultipartFile file, 
    		@RequestParam(value="fileDescription") String description, HttpSession session) throws IOException{
    	if(session.getAttribute("userid") == null) {
    		return "login";
    	}
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	
        if (result.hasErrors()) {
            System.out.println("validation errors");
            
            model.addAttribute("user", user);
 
            List<Document> documents = documentService.findAll();
            model.addAttribute("documents", documents);
             
            return "managedocuments";
        } else {
             
            System.out.println("Fetching file");
             
            model.addAttribute("user", user);
            fileBucket.setFile(file);
            saveDocument(fileBucket, user, description);
 
            return "redirect:/add-document";
        }
    }
    
    
    private void saveDocument(FileBucket fileBucket, User user, String fileDescription) throws IOException{    
        MultipartFile multipartFile = fileBucket.getFile();
        Document document = new Document(user.getID(),
        							multipartFile.getOriginalFilename(),
        							fileDescription,
        							multipartFile.getContentType(),
        							multipartFile.getBytes()
        							);
        documentService.saveDocument(document);
        System.out.println(document.getId());
    }
    
}