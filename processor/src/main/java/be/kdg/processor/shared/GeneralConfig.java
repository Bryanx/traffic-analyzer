package be.kdg.processor.shared;

import be.kdg.sa.services.CameraServiceProxy;
import be.kdg.sa.services.LicensePlateServiceProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Getter
@Setter
@EnableRabbit
@EnableScheduling
@Configuration
public class GeneralConfig {
    @Value("${buffer.config.time}")
    private int bufferTime;

    @Value("${fine.emission.time.between}")
    private int emissionFineTimeBetween;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CameraServiceProxy cameraServiceProxy(){
        return new CameraServiceProxy();
    }

    @Bean
    public LicensePlateServiceProxy licensePlateServiceProxy(){
        return new LicensePlateServiceProxy();
    }
}
