package com.sytoss.edu.library.services;

import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.producer.DMChangeProducer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DMChangeServiceTest {

    private final DMChangeProducer dmChangeProducer = mock(DMChangeProducer.class);

    private final DMChangeService dmChangeService = new DMChangeService(dmChangeProducer);

    @Test
    public void sendMessageTest() {
        DMEventMessage dmEventMessage = mock(DMEventMessage.class);
        dmChangeService.sendMessage(dmEventMessage);
        verify(dmChangeProducer).send(dmEventMessage);
    }
}
