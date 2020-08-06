package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    public static  List<User> userList = new ArrayList<>();

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
}
