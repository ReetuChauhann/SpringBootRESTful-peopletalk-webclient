package com.reetu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.reetu.beans.Message;
import com.reetu.beans.RecaptchaResponse;
import com.reetu.beans.User;

@Controller
public class MyController {
	
//	@Value("${google.recaptcha.verification.url}")
//	String url;
//	@Value("${google.recaptcha.secret}")
//	String secret_key;
	
	@Autowired
	JavaMailSender mailsender;
	
	 RestTemplate rt=new RestTemplate();
	 String URL="http://localhost:8998";
	 
	 @RequestMapping("/addUser")
	 public String adduser(@ModelAttribute User u, MultipartFile photo, Model model) {
		 String API="/adduser";
		 HttpHeaders header=new HttpHeaders();
		 header.setContentType(MediaType.MULTIPART_FORM_DATA);
		 
		 LinkedMultiValueMap<String, Object> data=new LinkedMultiValueMap<>();
		 data.add("User", u);
		 data.add("photo", convert(photo));
		 
		 HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data,header);
		 ResponseEntity<String> result=rt.postForEntity(URL+API, requestEntity, String.class);
		 if(result.getStatusCode()==HttpStatus.OK) {
			 model.addAttribute("addResult", u.getName()+"Successfully Added!");
		 }else {
			 model.addAttribute("addResult", u.getName()+ "Exists already!");
		 }
		 
		
		 return "index";
		 
		
	 }
	 
//	 @RequestMapping("/addUser")
//	 public String captcha(@RequestParam("g-recaptcha-response") String recaptcharesponse, Model model) {
//		 
//		 ResponseEntity<RecaptchaResponse> res=rt.exchange(url+"?secret="+secret_key+"&response"+recaptcharesponse, HttpMethod.PUT, null, RecaptchaResponse.class);
//		 RecaptchaResponse rs=res.getBody();
//		 if(rs.isSuccess()) {
//			 return "index";
//		 }else {
//			 model.addAttribute("addResult", "Verify the Captcha First!" );
//			 return "index";
//		 }
//		 
//	 }
	 
	 //convert file to FileSystemresource
	 public static FileSystemResource convert(MultipartFile file) {
  	   File convert=new File(file.getOriginalFilename());
  	   try {
  		   convert.createNewFile();
  		   FileOutputStream fos=new FileOutputStream(convert);
  		   fos.write(file.getBytes());
  		   fos.close();
  		   
			       
		} catch (Exception e) {
			e.printStackTrace();
		}
  	   
  	   return  new FileSystemResource(convert);
     }
	 
	 @RequestMapping("/login")
	 public String loginuser(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
		 String API="/userLogin/"+email+"/"+password;
		 String s=(String)rt.postForObject(URL+API,null,String.class);
		 if(s.equalsIgnoreCase("success")) {
			 API="/getuserbyemail/"+email;
			 User u=(User)rt.getForObject(URL+API, User.class);
			session.setAttribute("user", u);
			 return "profile";
			  }else {
				  model.addAttribute("loginResult", "Login Failed!");
				 
			  }
		 return "index";
		 
	 }
	 
	
	 
	 @RequestMapping("/profile")
	 public String profile(Model model, HttpSession session) {
		 User u=(User)session.getAttribute("user");
		 if(u!=null) {
			 return "profile";
		 }else {
			 model.addAttribute("loginResult", "Please Login!");
			 return "index";
		 }
	 }
	 //search user by address
	 @RequestMapping("/peoplesearch")
	 public String Peoplesearchbyaddress(String state, String city, String area, HttpSession session, Model model) {
		
		 User user=(User) session.getAttribute("user");
		 if(user!=null) {
			
			 model.addAttribute("state", state);
			 model.addAttribute("city", city);
			 model.addAttribute("area", area);
			 if(area.equals("")) {
				 area="nodata";
			 }
			 String API="/serachuser/"+state+"/"+city+ "/"+area+"/"+user.getEmail();
			 List<User> users=rt.getForObject(URL+API, List.class);
			 model.addAttribute("users", users);
			 return "peoplesearch";
		 }else {
			 model.addAttribute("loginResult", "Please Login!");
			 return "index";
		 }
		  
	 }
	 
	 //get image
	 @RequestMapping("/getimage")
	 public void getimage(String email, HttpServletResponse response) {
		 String API="/getimage/"+email;
		 try {
			   byte[] b=rt.getForObject(URL+API, byte[].class);
			   response.getOutputStream().write(b);
			   
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 }
	 
	 //talk
	 @RequestMapping("/talk")
	 public String peopletalk(String email, Model m, HttpSession session) {
		 User user=(User) session.getAttribute("user");
		 if(user!=null) {
			
			 if(email!=null) {
				    session.setAttribute("settedemail", email);
			 }else {
				 String e=(String) session.getAttribute("settedemail");
				 email=e;
			 }
			 String API="/getuserbyemail/"+email;
			 User u=(User)rt.getForObject(URL+API, User.class);
			 session.setAttribute("user_recevier",u);//here i'm adding the receiver user into the session also
			 User user_recevier=(User)session.getAttribute("user_recevier");
			 
			 
			String API1="/getmessg/"+user.getEmail()+"/"+user_recevier.getEmail();
			List<Message> smssg=rt.getForObject(URL+API1, List.class);
			session.setAttribute("smessg", smssg);
			
			String API2="/getmessg/"+user_recevier.getEmail()+"/"+ user.getEmail();
			List<Message> rmssg=rt.getForObject(URL+API2, List.class);
			session.setAttribute("rmessg", rmssg);
			
			return "talk";
		 }else {
			 m.addAttribute("loginResult", "Please Login!");
			 return "index";
		 }
		 
	}
	 
	 //add messg
	 @RequestMapping("/sendMessage")
	 public void addmessg(String message, MultipartFile file, Model model, HttpSession session, HttpServletResponse response) throws Exception {
		 User user=(User) session.getAttribute("user");
		 if(user!=null) {
		 String semail=user.getEmail();
		 User ruser=(User)session.getAttribute("user_recevier");
		 String remail=ruser.getEmail();
		 String filename=file.getOriginalFilename();
		 
		 Message messg=new Message();
		 messg.setSemail(semail);
		 messg.setRemail(remail);
		 messg.setFilename(filename);
		 messg.setMessg(message);
		
		 ResponseEntity<String> result;
		if(filename.equals("")) {
			String API="/addmessgwithoutfile";
			HttpEntity<Message> requestEntity=new HttpEntity<Message>(messg);
			result=rt.postForEntity(URL+API, requestEntity, String.class);
			
		}else {
			 String API="/addmessg";
			 HttpHeaders header=new HttpHeaders();
			 header.setContentType(MediaType.MULTIPART_FORM_DATA);
			 
			 LinkedMultiValueMap<String, Object> data=new LinkedMultiValueMap<>();
			 data.add("Message", messg);
			 data.add("file", convert(file));
			 
			 HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity=new HttpEntity<>(data,header);
		     result=rt.postForEntity(URL+API, requestEntity, String.class);
		}
		 if(result.getStatusCode()==HttpStatus.OK) {
			 session.setAttribute("msgResult", "Message Sent Successfully!");
		 }else {
			 session.setAttribute("msgResult", "Message Sent Fail!");
		 }
		 }
		 
		 response.sendRedirect("talk");
	 }
	 
	 
	 //update
	 @RequestMapping("/Updateprofile")
	 public String update(@ModelAttribute User us,Model model, HttpSession session) {
		 String API="/update";
	
		 
		
		 
		 HttpEntity<User> requestEntity=new HttpEntity<>(us);
		 ResponseEntity<String> status=rt.exchange(URL+API, HttpMethod.PUT, requestEntity, String.class);
		 if(status.getStatusCode()==HttpStatus.OK) {
			 model.addAttribute("status", us.getName()+ "Updated Successfully!");
		 }else {
			 model.addAttribute("status", us.getName() + "Not Updated!");
		 }
		 
		 
		 API="/getuserbyemail/"+us.getEmail();
		 User u=rt.getForObject(URL+API, User.class);
		 session.setAttribute("user", u);
		 
		 return "editprofile";
		//return "editprofile";
	 }
	 
	 //update image
	 @RequestMapping("/updateimage")
	 public String Updateimage(String email, MultipartFile image, Model model, HttpSession session) {
		 User user=(User) session.getAttribute("user");
		 String API="/update/"+email;
		 
		 HttpHeaders header=new HttpHeaders();
		 header.setContentType(MediaType.MULTIPART_FORM_DATA);
		 
		 LinkedMultiValueMap<String, Object> data=new LinkedMultiValueMap<>();
		 data.add("image", convert(image));
		 
		 HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity=new HttpEntity<>(data, header);
		 ResponseEntity<String> sts=rt.exchange(URL+API, HttpMethod.PUT, requestEntity, String.class);
		 if(sts.getStatusCode()==HttpStatus.OK) {
			 model.addAttribute("status",  "Updated Successfully!");
		 }else {
			 model.addAttribute("status",  "Not Updated!");
		 }
		 
		 API="/getuserbyemail/"+email;
		 User u=(User)rt.getForObject(URL+API, User.class);
		 session.setAttribute("user", u);
		 
		 return "editprofile";
		
	 }
	 
	 @RequestMapping("/editprofile")
	 public String update() {
		 return "editprofile";
	 }
//	 @RequestMapping("/editprofile")
//	 public String update() {
//		 return "editprofile";
//	 }
	 
	 @RequestMapping("/logout")
	 public String logout(HttpSession session) {
		 session.invalidate();
		 return "index";
		 
	 }
	 
	 @RequestMapping("/forgetpassword")
	 public String forgetpassword(String email, Model model) {
		        String API="/getpassword/"+email;
		        String p=rt.getForObject(URL+API, String.class);
		 try {
			 SimpleMailMessage mailmessage=new SimpleMailMessage();
			 mailmessage.setTo(email);
			 mailmessage.setSubject("forget password from talk people");
			 mailmessage.setText(p);
			 mailsender.send(mailmessage);
			 model.addAttribute("mail","Mail Sent Successfully!");
//			 MimeMessage mailmessage=mailsender.createMimeMessage();
//			 boolean multipart=true;
//			 MimeMessageHelper helper=new MimeMessageHelper(mailmessage, multipart);
//			 helper.setTo(email);
//			 helper.setSubject("forget password from talk people");
//			 helper.setText("changepassword.jsp"); //
//			 mailsender.send(mailmessage); 
//			 model.addAttribute("mail","Mail Sent Successfully!");
//			 
			 
			 } catch (Exception e) { 
				 
		 // TODO: handle exception
			e.printStackTrace();
			//model.addAttribute("mail","Something Went Wrong!");
				 
		}
		return "forgetpassword";
	 }
	 
	 @RequestMapping("/changepassword")
	 public String rpage() {
		 return "changepassword";
	 }
	 
	 @RequestMapping("/Changepassword")
	 public String changepassword(String oldp, String newp, String confp, Model model) {
		 String API="/updatepassword/"+oldp+"/"+newp+"/"+confp;
		 ResponseEntity<String> s= rt.exchange(URL+API, HttpMethod.PUT, null, String.class);
		 String r=s.getBody();
		 if(r.equalsIgnoreCase("success")) {
			 model.addAttribute("status", r);
		 }else {
			 model.addAttribute("status", r);
		 }
		 return "changepassword";
	 }
	 
	 @RequestMapping("/downloadFile")
	 public void downloadfile(int pid, String filename, HttpServletResponse response) {
		 String API="/downloadfile/"+pid;
		 try {
			    byte[] b=rt.getForObject(URL+API, byte[].class);
			  //for download file
				response.setContentType("APPLICATION/OCTET-STREAM");  
	            response.setHeader("Content-Disposition","attachment; filename="+filename); 
	            //for pdf view //have to add tergat="_blank" in the link for viewing in new tab
				//response.setContentType("APPLICATION/pdf"); 
	            //response.setHeader("inline","attachment; filename="+fileName); 
	            
	            response.getOutputStream().write(b);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 }
	 
	 
}
