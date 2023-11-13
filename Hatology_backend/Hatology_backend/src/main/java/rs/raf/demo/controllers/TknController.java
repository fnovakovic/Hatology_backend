package rs.raf.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Tkn;
import rs.raf.demo.utils.JwtUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/tkn")
public class TknController {

    private final JwtUtil jwtUtil;



    public TknController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }



    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody Tkn token){

        if(token.getToken() == "") {
            System.out.println("NEMA TOKENA");

            return ResponseEntity.ok(jwtUtil.generatePageToken());

        }else if(jwtUtil.isTokenExpiredd(token.getToken())){
            System.out.println("ISTEKAO TOKEN");
            return ResponseEntity.ok(jwtUtil.generatePageToken());
        }

        return ResponseEntity.ok(token.getToken());
    }
}
