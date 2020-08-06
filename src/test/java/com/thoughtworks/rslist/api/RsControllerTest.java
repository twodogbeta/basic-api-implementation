package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    String requestBody;
   public RsControllerTest() {
       requestBody = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"Tom\",\"age\": 19,\"gender\": \"female\",\"email\": \"a@thoughtworks.com\",\"phone\": \"18888888888\"}}";
    }
    @BeforeEach
    void setUp(){
        RsController.initRsEvent();
    }
    @Test
    void shouldGetRsList() throws Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
                .andExpect(jsonPath("$[0].keyWord").value("无分类"))
                .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
                .andExpect(jsonPath("$[1].keyWord").value("无分类"))
                .andExpect(jsonPath("$[2].eventName").value("第三条事件"))
                .andExpect(jsonPath("$[2].keyWord").value("无分类"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneEvent() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventName").value("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventName").value("第二条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventName").value("第三条事件"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
                .andExpect(jsonPath("$[0].keyWord").value("无分类"))
                .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
                .andExpect(jsonPath("$[1].keyWord").value("无分类"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("第二条事件"))
                .andExpect(jsonPath("$[0].keyWord").value("无分类"))
                .andExpect(jsonPath("$[1].eventName").value("第三条事件"))
                .andExpect(jsonPath("$[1].keyWord").value("无分类"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
                .andExpect(jsonPath("$[0].keyWord").value("无分类"))
                .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
                .andExpect(jsonPath("$[1].keyWord").value("无分类"))
                .andExpect(jsonPath("$[2].eventName").value("第三条事件"))
               // .andExpect(jsonPath("$[2].keyWord").value("无分类"))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")))
                .andExpect(status().isOk());

    }
    @Test
    void shouldAddOneRsEvent() throws Exception{
       // String requestJson = "{\"eventName\":\"第四条事件\", \"keyWord\":\"无分类\"}";

        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list/4"))
                .andExpect(jsonPath("$.eventName").value("添加一条热搜"))
                .andExpect(jsonPath("$.keyWord").value("娱乐"))
               /* .andExpect(jsonPath("$.user.name").value("xiaowang"))
                .andExpect(jsonPath("$.user.age").value(19))
                .andExpect(jsonPath("$.user.gender").value("female"))
                .andExpect(jsonPath("$.user.email").value("a@thoughtworks.com"))
                .andExpect(jsonPath("$.user.phone").value("18888888888"))*/
                .andExpect(jsonPath("$.user",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void modifyOneEvent() throws Exception {
        mockMvc.perform(put("/rs/list/1").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(jsonPath("$.eventName").value("添加一条热搜"))
                .andExpect(jsonPath("$.keyWord").value("娱乐"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOneEvent() throws Exception {
        mockMvc.perform(delete("/rs/list/2")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/2"))
                .andExpect(jsonPath("$.eventName").value("第三条事件"))
                .andExpect(jsonPath("$.keyWord").value("无分类"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldJustAddRsEventWhenUserNameExist() throws Exception {
        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Assertions.assertEquals(4,RsController.rsList.size());
        Assertions.assertEquals(0,UserController.userList.size());
    }
    @Test
    void shouldAddUserIntoUserListWhenUserNameNotExist() throws Exception {
        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Assertions.assertEquals(4,RsController.rsList.size());
        Assertions.assertEquals(1,UserController.userList.size());
    }

    @Test
    void allpostRequestShouldReturn201InRsController() throws Exception {
        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    }


