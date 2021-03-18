package com.example.demo.controller;
import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entities.Assign;
import com.example.demo.entities.CommenIdtList;
import com.example.demo.entities.Comment;
import com.example.demo.entities.CustomField;
import com.example.demo.entities.Issue;
import com.example.demo.entities.issueResponse.IssueCommentResponse;
import com.example.demo.entities.issueResponse.UserComments;
import com.example.demo.entities.updateComment.UpdateComment;
import com.example.demo.model.Reponse;
import com.example.demo.entities.*;

@RestController
@RequestMapping("/response")
public class AtlassianController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AtlassianController.class);
	@Value("${jira.uri}")
	String jiraUri;
	@Autowired
	private RestTemplate restTemplate;
	
	
	HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }};
		}   
     
//-------------------------------------createIssue*---------------------------------------------------
	
     @PostMapping(
    	        value = "/createIssue",
    	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> createIssue(@RequestBody Issue issue) 
           {
     	        Reponse resp = new Reponse();
     	        resp.setNombre("Created Issue");
     	        resp.setRegistros_status("SUCCESS");
     	        LOGGER.info("Creating issue ");
    	    	HttpHeaders headers = new HttpHeaders();
    	        headers.setContentType(MediaType.APPLICATION_JSON);
    	        HttpEntity<Issue> entity =new HttpEntity<Issue>(issue,headers);
    	        try {
    	        restTemplate.exchange(
    	        		jiraUri+"/issue", HttpMethod.POST, entity, Issue.class).getBody();
    	            }
    	        
    	        catch(Exception e) {
    	        		LOGGER.error("DATA NOT FOUND");
    	            	resp.setRegistros_status("FAILED");
    	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
    	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
    	        	  
    	        	}
    	        	
    	        return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
    	    	 
    	     }
//---------------------------------Assign User*-------------------------------------------
     
     @PutMapping(
 	        value = "/assignee/issueKey={issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
 	 public ResponseEntity<Reponse> assignee(@RequestBody Assign assign, @PathVariable String issueKey) {
    	    Reponse resp = new Reponse();
	        resp.setNombre("Trying to assign: "+assign.getName());
	        resp.setRegistros_status("SUCCESS");
	        LOGGER.info("Assign started in issue: "+issueKey);
 	    	 HttpHeaders headers = new HttpHeaders();
 	         headers.setContentType(MediaType.APPLICATION_JSON);
 	         HttpEntity<Assign> entity =new HttpEntity<Assign>(assign,headers);
 	        try {
 	        restTemplate.exchange(
 	        		jiraUri+"/issue/"+issueKey+"/assignee", HttpMethod.PUT, entity, Issue.class).getBody();
 	           }
 	       catch(Exception e) {
       		LOGGER.error("FAIL TO ASSIGN: "+assign.getName());
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
       	  
         	}
 	       return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
 	        
 	    }
     
     
     
     
   //----------------------------Delete Issue-----------------------------------
     
     
     
     @DeleteMapping(
 	        value = "/deleteIssue/{issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Reponse> deleteIssue(@PathVariable String issueKey ) 
        {
  	        Reponse resp = new Reponse();
  	        resp.setNombre("Trying to delete Issue: "+issueKey);
  	        resp.setRegistros_status("SUCCESS");
  	        LOGGER.info("Creating issue ");
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<String> entity =new HttpEntity<String>(headers);
 	        try {
 	        restTemplate.exchange(
 	        		jiraUri+"/issue/"+issueKey, HttpMethod.DELETE, entity, String.class).getBody();
 	            }
 	        
 	        catch(Exception e) {
 	        		LOGGER.error("DATA NOT FOUND");
 	            	resp.setRegistros_status("FAILED");
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
 	        	  
 	        	}
 	        	
 	        return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
 	    }
     
   //--------------------------------getIssue----------------------------------------
     
     @GetMapping(
  	        value = "/getIssue/{issueKey}",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
   public String getIssue(@PathVariable String issueKey ) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Get Issue: "+issueKey);
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Trying to get Issue: "+issueKey);
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<String> entity =new HttpEntity<String>(headers);
  	        try {
  	        return restTemplate.exchange(
  	        		jiraUri+"/issue/"+issueKey, HttpMethod.GET, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("DATA NOT FOUND");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "ISSUE: "+issueKey+" NOT FOUND";
  	        	  
  	        	}
  	        	
  	     }
      
      
     
  //------------------------EiditIssue*-----------------------------------
     
   
     
     @PutMapping(
 	        value = "/editIssue/{issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Reponse> editIssue(@RequestBody Issue issue, @PathVariable String issueKey) 
        {
  	        Reponse resp = new Reponse();
  	        resp.setNombre("Created Issue");
  	        resp.setRegistros_status("SUCCESS");
  	        LOGGER.info("Creating issue ");
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<Issue> entity =new HttpEntity<Issue>(issue,headers);
 	        try {
 	        restTemplate.exchange(
 	        		jiraUri+"/issue/"+issueKey, HttpMethod.PUT, entity, Issue.class).getBody();
 	            }
 	        
 	        catch(Exception e) {
 	        		LOGGER.error("DATA NOT FOUND");
 	            	resp.setRegistros_status("FAILED");
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return new ResponseEntity<Reponse>(resp, HttpStatus.FORBIDDEN);
 	        	  
 	        	}
 	        	
 	        return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
 	    	 
 	     }
     
 //----------------------------------------addComment------------------------------------
     
     @PostMapping(
  	        value = "/addCommentByAdministrators/{issueKey}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Reponse> addComment(@RequestBody Comment comment, @PathVariable String issueKey) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Add Comment");
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Adding comment ");
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<Comment> entity =new HttpEntity<Comment>(comment,headers);
  	        try {
  	        restTemplate.exchange(
  	        		jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.POST, entity, Comment.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("Fail to add comment");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
  	        	  
  	        	}
  	        	
  	        return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
  	    	 
  	     }
     
  //--------------------------------------------------Get Comment------------------------------------
     
     @GetMapping(
   	        value = "/getComment/issueKey={issueKey}/commentId={commentId}",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getComment(@PathVariable String issueKey, @PathVariable String commentId ) 
          {
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get Comment: "+commentId);
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get Comment: "+commentId);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        try {
   	        return restTemplate.exchange(//https://eklas.atlassian.net/rest/api/3/issue/MFP-39/comment/10038
   	        		jiraUri+"/issue/"+issueKey+"/comment/"+commentId, HttpMethod.GET, entity, String.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("COMMENT NOT FOUND");
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "COMMENT: "+commentId+" NOT FOUND";
   	        	  
   	        	}
   	        	
   	     }
     
     
 // ---------------------------------------Delete comment*-----------------------------------
     
     
     @DeleteMapping(
    	        value = "/deleteComment/issueKey={issueKey}/commentId={commentId}",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String deleteComment(@PathVariable String issueKey, @PathVariable String commentId ) 
           {
     	        Reponse resp = new Reponse();
     	        resp.setNombre("Get Issue: "+issueKey);
     	        resp.setRegistros_status("SUCCESS");
     	        LOGGER.info("Trying to delete comment: "+commentId);
    	    	HttpHeaders headers = new HttpHeaders();
    	        headers.setContentType(MediaType.APPLICATION_JSON);
    	        HttpEntity<String> entity =new HttpEntity<String>(headers);
    	        try {
    	        return restTemplate.exchange(
    	        		jiraUri+"/issue/"+issueKey+"/comment/"+commentId, HttpMethod.DELETE, entity, String.class).getBody();
    	            }
    	        
    	        catch(Exception e) {
    	        		LOGGER.error("COMMENT NOT FOUND");
    	            	resp.setRegistros_status("FAILED");
    	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
    	            	return "COMMENT: "+commentId+" NOT FOUND";
    	        	  
    	        	}
    	        	
    	     }
      
     
     
     //--------------------Get edit issue meta------------------------------
     
     @GetMapping(
    	        value = "/getEditIssueMeta/issueKey={issueKey}",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String GetEditIssueMeta(@PathVariable String issueKey ) 
           {
     	        Reponse resp = new Reponse();
     	        resp.setNombre("Get issue meta: "+issueKey);
     	        resp.setRegistros_status("SUCCESS");
     	        LOGGER.info("Trying to get issue Meta: "+issueKey);
    	    	HttpHeaders headers = new HttpHeaders();
    	        headers.setContentType(MediaType.APPLICATION_JSON);
    	        HttpEntity<String> entity =new HttpEntity<String>(headers);
    	        try {
    	        return restTemplate.exchange(//https://eklas.atlassian.net/rest/api/3/issue/MFP-39/comment/10038
    	        		jiraUri+"/issue/"+issueKey+"/editmeta", HttpMethod.GET, entity, String.class).getBody();
    	            }
    	        
    	        catch(Exception e) {
    	        		LOGGER.error("ISSUE META NOT FOUND");
    	            	resp.setRegistros_status("FAILED");
    	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
    	            	return "ISSUE: "+issueKey+" NOT FOUND";
    	        	  
    	        	}
    	        	
    	     }
      
     
  //----------------------Get Remote Issue Link-------------------------------------------------
    
     @GetMapping(
 	        value = "/getRemoteIssueLink/issueKey={issueKey}",
 	        produces = MediaType.APPLICATION_JSON_VALUE)
  public String getRemoteIssueLink(@PathVariable String issueKey ) 
        {
  	        Reponse resp = new Reponse();
  	        resp.setNombre("Get remote issue link: "+issueKey);
  	        resp.setRegistros_status("SUCCESS");
  	        LOGGER.info("Trying to get remote issue link: "+issueKey);
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<String> entity =new HttpEntity<String>(headers);
 	        try {
 	        return restTemplate.exchange(//rest/api/2/issue/{issueIdOrKey}/remotelink
 	        		jiraUri+"/issue/"+issueKey+"/remotelink", HttpMethod.GET, entity, String.class).getBody();
 	            }
 	        
 	        catch(Exception e) {
 	        		LOGGER.error("ISSUE NOT FOUND");
 	            	resp.setRegistros_status("FAILED");
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return "ISSUE: "+issueKey+" NOT FOUND";
 	        	  
 	        	}
 	        	
 	     }
   
//-------------------------------Get all users---------------------------------------
     
     @GetMapping(
   	        value = "/getAllUsers",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllUsers( ) 
          {
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get all users");
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get all users:");
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        try {
   	        return restTemplate.exchange(
   	        		jiraUri+"/3/users/search", HttpMethod.GET, entity, String.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("DATA NOT FOUND");
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "USERS NOT FOUND";
   	        	  
   	        	}
   	        	
   	     }
     
     
     
    //---------------------------get Comments of specific issue------------------------------------------------
    
     
     @GetMapping(
  	        value = "/getIssueComments/issueKey={issueKey}",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
   public String getCommentOfIssue(@PathVariable String issueKey ) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Get comment(s) of Issue: "+issueKey);
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Trying to get Comment of Issue: "+issueKey);
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<String> entity =new HttpEntity<String>(headers);
  	        try {
  	        return restTemplate.exchange(
  	        		jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.GET, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("COMMENT(S) NOT FOUND");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "COMMENT IF ISSUE: "+issueKey+" NOT FOUND";
  	        	  
  	        	}
  	        	
  	     }
      
      
     
  //------------------------  Get comment list     -----------------------------------
     
     
     @PostMapping(
  	        value = "/getCommentList",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
   public String getCommentList(@RequestBody CommenIdtList comment) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Get Comment List");
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Getting comment(s) respect to your ids ");
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<CommenIdtList> entity =new HttpEntity<CommenIdtList>(comment,headers);
  	        try {
  	        return restTemplate.exchange(
  	        		jiraUri+"/comment/list", HttpMethod.POST, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("Fail to add comment");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND).toString();
  	            	// "COMMENTS NOT FOUND";
  	            			
  	        	  
  	        	}
  	        	
  	       // return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
  	    	 
  	     }
       
//-------------------------Post custom field----------------------------------
     
     
     @PostMapping(
  	        value = "/addCustomField",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
   public String getCommentList(@RequestBody CustomField customField) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Add Custom Field");
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Adding custom fields ");
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<CustomField> entity =new HttpEntity<CustomField>(customField,headers);
  	        try {
  	        return restTemplate.exchange(
  	        		jiraUri+"/field", HttpMethod.POST, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("Fail to add custom fileds");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND).toString();
  	            	// "COMMENTS NOT FOUND";
  	            			
  	        	  
  	        	}
  	        	
  	       // return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
  	    	 
  	     }
       
     
     //-----------------------------Update Comment----------------------------
     
     @PutMapping(
  	        value = "/updateComment/issueKey={issueKey}/commentId={commentId}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
   public  ResponseEntity<Reponse> updateComment(@RequestBody UpdateComment updateComment, @PathVariable String issueKey, @PathVariable String commentId) 
         {
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Update Comment: "+commentId);
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Updating Comment: "+commentId);
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<UpdateComment> entity =new HttpEntity<UpdateComment>(updateComment,headers);
  	        try {
  	        restTemplate.exchange(// https://eklas.atlassian.net/rest/api/3/issue/MFP-27/comment/10047
  	        		jiraUri+"/issue/"+issueKey+ "/comment/"+commentId, HttpMethod.PUT, entity, UpdateComment.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("Fail to add custom fileds");
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
  	            	// "COMMENTS NOT FOUND";
  	            			
  	        	  
  	        	}
  	        	
  	       return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
  	    	 
  	     }
       
    //-------------------delete comment ------------
     
     @DeleteMapping(
   	        value = "/deleteComment/issueKey={issueKey}/commentId={commentId}",
   	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Reponse> deleteComment(@RequestBody UpdateComment updateComment, @PathVariable String issueKey, @PathVariable String commentId) 
          {
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Delete Comment: "+commentId);
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Deleting Comment: "+commentId);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<UpdateComment> entity =new HttpEntity<UpdateComment>(updateComment,headers);
   	        try {
   	        restTemplate.exchange(// https://eklas.atlassian.net/rest/api/3/issue/MFP-27/comment/10047
   	        		jiraUri+"/issue/"+issueKey+ "/comment/"+commentId, HttpMethod.DELETE, entity, UpdateComment.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("Fail to delete comment: "+commentId);
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
   	            	// "COMMENTS NOT FOUND";
   	            			
   	        	  
   	        	}
   	        	
   	       return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
   	    	 
   	     }
     
     
     
     
    // ------------------------find commnet by user.........................................
     
     
     @GetMapping(
   	        value = "/getCommentsByUserName/issueKey={issueKey}/userName={userName}",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
    public UserComments getUserComment(@PathVariable String issueKey, @PathVariable String userName) 
          {
    	
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get comment(s) of Issue: "+issueKey);
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get Comment of Issue: "+issueKey);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        UserComments userComments=new UserComments();
   	        try {
   	         
   	        	IssueCommentResponse issueCommentResponse =restTemplate.exchange(
   	        			jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.GET, entity,IssueCommentResponse.class).getBody();
   	        	
   	        	for(com.example.demo.entities.issueResponse.Comment comment:issueCommentResponse.getComments())
   	        	{
   	        		if(comment.getAuthor().getDisplayName().equals(userName))
   	        		{
   	        			userComments.getList().add(comment);
   	        		}
   	        		
   	        	}
   	        	
   	        	LOGGER.info("Total comments: "+issueCommentResponse.getTotal());
   	        	return userComments;
   	        	//return issueCommentResponse.toString();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("COMMENT(S) NOT FOUND");
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	//return "COMMENT IF ISSUE: "+issueKey+" NOT FOUND";
   	            	return userComments;
   	        	  
   	        	}
   	        	
   	     }
       
     
    
}