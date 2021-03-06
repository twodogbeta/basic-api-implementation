package com.thoughtworks.rslist.api;

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
                .andExpect(jsonPath("$",hasKey("user")))
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
        String requestBody1 = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"pop\",\"age\": 19,\"gender\": \"female\",\"email\": \"a@thoughtworks.com\",\"phone\": \"18888888888\"}}";
        mockMvc.perform(post("/rs/event").content(requestBody1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Assertions.assertEquals(4,RsController.rsList.size());
        Assertions.assertEquals(1,UserController.userList.size());
    }
    @Test
    void shouldAddUserIntoUserListWhenUserNameNotExist() throws Exception {
        String requestBody2 = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"Lucy\",\"age\": 19,\"gender\": \"female\",\"email\": \"a@thoughtworks.com\",\"phone\": \"18888888888\"}}";
        mockMvc.perform(post("/rs/event").content(requestBody2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Assertions.assertEquals(4,RsController.rsList.size());
        Assertions.assertEquals(2,UserController.userList.size());
    }

    @Test
    void allPostRequestShouldReturn201InRsController() throws Exception {
      /* RsEvent rsEvent = new RsEvent("事件一","经济",null);
        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(rsEvent);*/
        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotHasUserWhenGetRsList() throws Exception {
       mockMvc.perform(get("/rs/list"))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0]",not(hasKey("user"))));
    }

    @Test
    void shouldReturnBadRequestWhenIndexOutOfBound() throws Exception {
       mockMvc.perform(get("/rs/list/10"))
               .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void shouldReturnBadRequestWhenIndexOutOfBoun() throws Exception {
        mockMvc.perform(get( "/rs/list?start=8&end=2"))
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }
    @Test
    void shouldReturnExceptionWhenOneEventValidUser() throws Exception {
        String requestBody1 = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"pop\",\"age\": 2,\"gender\": \"female\",\"email\": \"a@thoughtworks.com\",\"phone\": \"18888888888\"}}";
        mockMvc.perform(post("/rs/event").content(requestBody1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid param")))
                .andExpect(status().isBadRequest());

    }

    }


