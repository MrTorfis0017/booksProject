package com.sytoss.edu.library.services;

import com.sytoss.edu.library.model.DMEventMessage;
import com.sytoss.edu.library.producer.DMChangeProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DMChangeService {

    private final DMChangeProducer producer;

    public void sendMessage(DMEventMessage eventMessage) {
        producer.send(eventMessage);
    }
}
