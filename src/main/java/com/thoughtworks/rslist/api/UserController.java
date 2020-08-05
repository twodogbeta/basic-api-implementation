package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    public static  Set<User> usersSet = new HashSet<>();

    @PostMapping("/user")
    public static void register(@RequestBody @Valid User user){
        if (!usersSet.contains(user))
        usersSet.add(user);
    }


}
