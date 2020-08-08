package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.utils.ListOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static  List<User> userList = new ArrayList<>();
    private User user = new User("pop", "male", 40, "tom@gmail.com", "15800000000");

    public UserController(){
        userList = new ArrayList<>();
        userList.add(user);
    }

    @PostMapping("/user")
    public static ResponseEntity register(@RequestBody @Valid User user){
        for (User u : userList) {
            if (u.getName() == user.getName())
                return ResponseEntity.created(null).build();
        }
        userList.add(user);
        String headers = String.valueOf(userList.indexOf(user));
        return ResponseEntity.created(URI.create(headers)).build();
    }
    @GetMapping("/users")
    public static ResponseEntity<List> addUsers(@RequestParam( required = false) Integer formIndex,
                                                @RequestParam ( required = false)Integer toIndex){
        return ListOperation.getListByIndex(formIndex,toIndex,userList);
    }

}
