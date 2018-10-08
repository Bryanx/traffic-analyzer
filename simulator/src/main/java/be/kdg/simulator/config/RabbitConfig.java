package be.kdg.simulator.config;

import be.kdg.simulator.model.CameraMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@EnableRabbit
@Getter
@Setter
@ConfigurationProperties(prefix = "queue")
@Configuration
public class RabbitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);
    private String name = "camera-message-queue";

    public void setName(String name) {
        if (!name.equals("")) this.name = name;
    }

    @Bean
    public Queue queue() {
        LOGGER.info("Creating new queue with name: " + name);
        return new Queue(name, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerMarshallingMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Before sending a message it is converted to xml and the content-type is changed to application/xml.
     */
    @Bean
    public MarshallingMessageConverter producerMarshallingMessageConverter() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(CameraMessage.class);
        MarshallingMessageConverter marshallingMessageConverter = new MarshallingMessageConverter(jaxb2Marshaller);
        marshallingMessageConverter.setContentType("application/xml");
        return marshallingMessageConverter;
    }
}
