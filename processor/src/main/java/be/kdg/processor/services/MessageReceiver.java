package be.kdg.processor.services;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "camera-message-queue")
public class MessageReceiver {

    @RabbitHandler
    public void receive(String msg) {
        System.out.println("Received " + msg);
    }
}
