package com.gym.jms;

import com.gym.dto.requests.TrainingWorkloadRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadProducer {
    private final JmsTemplate jmsTemplate;

    public TrainerWorkloadProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(TrainingWorkloadRequest request) {
        jmsTemplate.convertAndSend(
                "trainer.workload.queue",
                request
        );
    }
}
