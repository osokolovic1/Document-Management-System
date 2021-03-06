package com.etfbp.dms.controllers;
 
import static org.assertj.core.api.Assertions.in;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import javax.transaction.Transactional;
import javax.validation.Valid;

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
import com.etfbp.dms.models.Grupa;
import com.etfbp.dms.models.News;
import com.etfbp.dms.models.NewsToShow;
import com.etfbp.dms.models.Role;
import com.etfbp.dms.repo.UserRepository;
import com.etfbp.dms.services.DocumentService;
import com.etfbp.dms.services.GroupService;
import com.etfbp.dms.services.NewsService;
import com.etfbp.dms.services.UserService;
 
 
 
@Controller
@Transactional
public class WebController {
 
	@Autowired
	UserService userService;
	
	@Autowired
	DocumentService documentService;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	NewsService newsService;
	
	
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
    			session.setAttribute("name", user.getName());
    			session.setAttribute("lastname", user.getLastName());
    			session.setAttribute("email", user.getEmail());
    			session.setAttribute("roleid", user.getRole().getRole());
    			
    			model.addAttribute("user", user);
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
    @RequestMapping(value= "/contact", method=RequestMethod.GET)
    public String contact(Model model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
        model.addAttribute("user", new User());
        
        return "contact";
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
        User novi = userService.findById(user.getID());
        novi.setRole(new Role("student"));
        
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
    @RequestMapping(value = { "/update-document-{docId}" }, method = RequestMethod.GET)
    public String updateDocument(@PathVariable String docId, ModelMap model, HttpServletResponse response, HttpSession session) throws IOException {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
    	Document document = documentService.findById(Integer.parseInt(docId));
    	if(document.getType().equals("text/plain"))
    	{
    		model.addAttribute("documentContent", new String(document.getContent()));
        	model.addAttribute("docId", docId);
            
      
            
      
            return "updateDocument";
    	}
    	return "redirect:/";
    	
    }
    @RequestMapping(value = { "/update-document-{docId}" }, method = RequestMethod.POST)
    public String updateDocumentPost(@PathVariable String docId, ModelMap model,@RequestParam(value="fileContent") String content, HttpServletResponse response, HttpSession session) throws IOException {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
    	Document document = documentService.findById(Integer.parseInt(docId));
    	documentService.updateDocument(document.getId(), content.getBytes());
        
  
        
  
        return "redirect:/";
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
    	if(document == null)
    		return "error";
    	
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
    
    @RequestMapping(value = { "/delete-my-document-{docId}" }, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable String docId, HttpSession session) {
        if(session.getAttribute("userid") == null)
        	return "redirect:/";
        
        documentService.deleteById(Integer.parseInt(docId));
        return "redirect:/my-documents";
    }
    
    @RequestMapping(value = {"/news"}, method = RequestMethod.GET)
    public String news (ModelMap model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	
    	List<News> newsList = newsService.findAll();
    	List<NewsToShow> newsToShow = new ArrayList<>();
    	for(int i = 0; i < newsList.size(); i++) {
    		newsToShow.add(new NewsToShow(newsList.get(i).getId(), newsList.get(i).getNews(), newsList.get(i).getUserName()));
    	}
        model.addAttribute("newsList", newsToShow);
        
        
        return "news";
    }
    
    @RequestMapping(value = { "/my-documents" }, method = RequestMethod.GET)
    public String myDocuments(ModelMap model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	 
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
 
        List<Document> documents = documentService.findAllByUserId(userId);
        
        Set<User> allUsers = userService.findAll();
        Set<Grupa> allGroups = groupService.findAll();
        
        //Izbacivanje korisnika koji trazi listu korisnika
        Iterator<User> iterator = allUsers.iterator();
        while (iterator.hasNext()) {
            User element = iterator.next();
            if (element.getID().equals(session.getAttribute("userid"))) {
                iterator.remove();
                break;
            }
        }

        model.addAttribute("groups",allGroups);
        model.addAttribute("users", allUsers);
        model.addAttribute("documents", documents);
        
        return "myDocuments";
    }
    
    @RequestMapping(value = { "/add-news" }, method = RequestMethod.GET)
    public String addNews(ModelMap model, HttpSession session, HttpServletRequest request) throws IOException{
    	
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	
    	
    	
    	return "addNews";
    }
    @RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
    public String changePassword(ModelMap model, HttpSession session, HttpServletRequest request) throws IOException{
    	
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	
    	
    	
    	return "changePassword";
    }
    
    @RequestMapping(value = { "/changePassword" }, method = RequestMethod.POST)
    public String changePasswordPost(@RequestParam(value="oldPass")String oldPass, @RequestParam(value="newPass") String newPass,
    									@RequestParam(value="newPass2") String newPass2, ModelMap model, HttpSession session,
    									HttpServletRequest request) throws IOException{
    	
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	if(!user.getPassword().equals(oldPass))
    	{
    		JOptionPane.showMessageDialog(null, "Insert correct password");
    		return "changePassword";
    	}
    	else if (!newPass.equals(newPass2))
    	{
    		JOptionPane.showMessageDialog(null, "You have to insert new password twice");
    		return "changePassword";
    	}
    	
    	
    	userService.updatePassword(user.getID(), newPass);
    	JOptionPane.showMessageDialog(null, "You successfully changed your password");
    	return "home";
    }
    
    @RequestMapping(value = { "/add-document" }, method = RequestMethod.GET)
    public String addDocuments(ModelMap model, HttpSession session) {
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute("user", user);
    	 
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
 
        List<Document> docs = documentService.findAllByUserPermissionNotOwning(user.getID());

        model.addAttribute("documents", docs);

        return "managedocuments";
    }
    
    @RequestMapping(value = { "/add-news" }, method = RequestMethod.POST)
    public String addNews(@RequestParam(value="newsText") String newsText, ModelMap model, HttpSession session, HttpServletRequest request) throws IOException{
    	
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
    	User user = userService.findById(userId);
    	model.addAttribute(user);
    	
    	News news = new News (newsText, user);
    	newsService.saveNews(news);
    	
    	List<News> newsList = newsService.findAll();
    	List<NewsToShow> newsToShow = new ArrayList<>();
    	for(int i = 0; i < newsList.size(); i++) {
    		newsToShow.add(new NewsToShow(newsList.get(i).getId(), newsList.get(i).getNews(), newsList.get(i).getUserName()));
    	}
        model.addAttribute("newsList", newsToShow);
    	
    	return "news";
    }
    
    @RequestMapping(value = { "/add-document" }, method = RequestMethod.POST)
    public String addDocuments(@Valid FileBucket fileBucket, BindingResult result, ModelMap model, @RequestParam("file") MultipartFile file, 
    		@RequestParam(value="fileDescription") String description, HttpSession session, HttpServletRequest request) throws IOException{
    	
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}
    	
    	int userId = (int)session.getAttribute("userid");
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

            saveDocument(fileBucket, user, description, sharedWithUsers, sharedWithGroups);
 
            return "redirect:/my-documents";
        }
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
     
    
    /*ne brisati, sluzi za rad na formama*/
    @RequestMapping(value= {"/template"}, method=RequestMethod.GET)
    public String test(Model model) {
    	
        model.addAttribute("user", new User());
        return "template";
    }
    
}