package com.daniel.falcon.interview.controller;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.service.MessageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Before
    public void setUp() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void addMessageShouldThrowExceptionToEmptyBody() throws Exception {
        ResultActions result = mockMvc.perform(post("/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addMessageShouldReturnOkToStringBody() throws Exception {
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("MessageText"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllShouldReturnsMessages() throws Exception {
        Message expectedMessage = new Message();
        expectedMessage.setMessageText("Message text");
        expectedMessage.setId(2L);
        Mockito.when(messageService.getAllMessages()).thenReturn(Collections.singletonList(expectedMessage));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/get").accept(
                MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals("[{\"id\":2,\"messageText\":\"Message text\"}]", resultString);
    }

}
