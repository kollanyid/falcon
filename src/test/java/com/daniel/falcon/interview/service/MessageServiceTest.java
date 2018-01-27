package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllMessagesShouldReturnsExpectedList() {
        Message expectedMessage = new Message();
        expectedMessage.setId(1L);
        expectedMessage.setMessageText("Expected message text");
        List<Message> expectedList = Arrays.asList(expectedMessage);
        Mockito.when(messageRepository.findAll()).thenReturn(expectedList);
        List<Message> result = messageService.getAllMessages();
        Assert.assertTrue(expectedList.size() == result.size());
        Assert.assertEquals(expectedMessage, result.get(0));
    }

    @Test
    public void addMessageShouldSendExpectedMessageToMock() {
        Message expectedMessage = new Message();
        expectedMessage.setMessageText("Expected message text");
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        messageService.addMessage("Expected message text");
        verify(messageRepository).save(messageArgumentCaptor.capture());
        Assert.assertEquals(expectedMessage, messageArgumentCaptor.getValue());
    }
}
