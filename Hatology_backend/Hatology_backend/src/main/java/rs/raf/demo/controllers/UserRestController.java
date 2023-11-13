package rs.raf.demo.controllers;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private User currUser;


    public UserRestController(UserService userService){
        this.userService = userService;

    }

//    @DeleteMapping(value = "/delete/{email}")
//    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
//        String emaill[] = email.split("=");
//        user2Service.deleteByEmail(emaill[1]);
//        return ResponseEntity.ok().build();
//    }
//
//
//    @PostMapping(value = "/search",consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User2> searchCurrUser(@RequestBody User2 user){
//
//        return ResponseEntity.ok(user2Service.findByEmail(user.getEmail()));
//    }

    @GetMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> check(){

        currUser =   userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (currUser.getAdminPermission().equals("1")) {
            User usr = new User();
            usr.setAdminPermission(userService.findByEmail(currUser.getEmail()).getAdminPermission());
            return ResponseEntity.ok(usr);
        }
        return ResponseEntity.status(403).build();
    }

}
