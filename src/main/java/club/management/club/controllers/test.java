package club.management.club.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class test {
    @GetMapping("/get")
    public String sayHello(){
        return "hello" ;
    }
    @PostMapping("/post")
    public String resendMessage(@RequestBody String message){
        return message;
    }
}
