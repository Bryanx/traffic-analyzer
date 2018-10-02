package be.kdg.processor.config;

import be.kdg.sa.services.CameraServiceProxy;
import be.kdg.sa.services.LicensePlateServiceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdPartyConfig {

    @Bean
    public CameraServiceProxy cameraServiceProxy(){
        return new CameraServiceProxy();
    }

    @Bean
    public LicensePlateServiceProxy licensePlateServiceProxy(){
        return new LicensePlateServiceProxy();
    }
}
