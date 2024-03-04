package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.*;
import rs.raf.demo.services.ContactService;
import rs.raf.demo.services.EmailService;
import rs.raf.demo.services.UserService;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;
    private final EmailService emailService;
    private UserService userService;
    private User currUser;




    public ContactController(ContactService contactService, EmailService emailService, UserService userService) {
        this.contactService = contactService;
        this.emailService = emailService;
        this.userService = userService;
    }


    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> getAllProducts(){
        currUser =   userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getAdminPermission().equals("1")){ //Provera da li ima permisiju
        List<Contact> products = contactService.findAllContacts();//currUser.getUserId()
        return ResponseEntity.ok(products);
         }
        return ResponseEntity.status(403).build();
    }


    @PostMapping(value ="/createContact",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> handleFileUpload(@RequestBody Contact contact) {

            Contact c = new Contact();
            c.setName(contact.getName());
            c.setEmail(contact.getEmail());
            c.setComment(contact.getComment());
            contactService.save(c);
//            emailService.sendEmail(contact.getEmail(),"ODGOVOR","NEKI TEXT");
                return ResponseEntity.ok(HttpStatus.OK);
    }



    @PostMapping(value ="/delete",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> delete(@RequestBody Contact contact) {
         currUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (currUser.getAdminPermission().equals("1")) {

        contactService.deleteByUserId(contact.getUserId());

        return ResponseEntity.ok(HttpStatus.OK);

          }
        return ResponseEntity.status(403).build();

    }
}
