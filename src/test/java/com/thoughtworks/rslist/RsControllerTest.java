package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
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
                .andExpect(jsonPath("$[2].keyWord").value("无分类"))
                .andExpect(status().isOk());

    }
    @Test
    void shouldAddOneRsEvent() throws Exception{
       // String requestJson = "{\"eventName\":\"第四条事件\", \"keyWord\":\"无分类\"}";
        RsEvent rsEvent = new RsEvent("第四条事件", "娱乐");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/list").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/4")).andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value("第四条事件"))
                .andExpect(jsonPath("$.keyWord").value("娱乐"));
    }

    @Test
    void modify_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("A股高开低走", "财经");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/list/1").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(jsonPath("$.eventName").value("A股高开低走"))
                .andExpect(jsonPath("$.keyWord").value("财经"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_one_event() throws Exception {
        mockMvc.perform(delete("/rs/list/2")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/2"))
                .andExpect(jsonPath("$.eventName").value("第三条事件"))
                .andExpect(jsonPath("$.keyWord").value("无分类"))
                .andExpect(status().isOk());
    }

}
