package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
       // UserController.userList.clear();

    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Jim", "male", 18, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1,UserController.userList.size());
    }
    @Test
    void nameShouldNotNull() throws Exception {
        User user = new User(null, "male", 18, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error",is("invalid param")));
    }
    @Test
    void genderShouldNotNull() throws Exception {
        User user = new User("Jim", null, 18, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldNotLess18() throws Exception {
        User user = new User("Jim", "male", 17, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void ageShouldNotMoreThan100() throws Exception {
        User user = new User("Jim", "male", 101, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void emailShouldInLine() throws Exception {
        User user = new User("Jim", "male", 19, "qijinhao", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void phoneNumberShouldValid() throws Exception {
        User user = new User("Jim", "male", 19, "qijinhaoup@163.com", "1383232366232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void allpostRequestShouldReturn201InUserController() throws Exception {
        User user = new User("Jim", "male", 18, "qijinhaoup@163.com", "13832323232");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());


    }
    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> usersList = new ArrayList<>();
        User user = new User("pop", "male", 40, "tom@gmail.com", "15800000000");
        usersList.add(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String usersSerialization = objectMapper.writeValueAsString(usersList);
        mockMvc.perform(get("/users"))
                .andExpect(content().string(usersSerialization))
                .andExpect(status().isOk());

    }




}