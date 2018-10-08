package be.kdg.processor.shared;

import be.kdg.processor.camera.message.CameraMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MarshallingMessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Here the Jaxb2Marshaller is attached to the rabbitMQ config.
 * When a message is received the xml is converted to a CameraMessage.
 */
@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

    @Bean
    public MarshallingMessageConverter producerMarshallingMessageConverter() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(CameraMessage.class);
        return new MarshallingMessageConverter(jaxb2Marshaller);
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(producerMarshallingMessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
