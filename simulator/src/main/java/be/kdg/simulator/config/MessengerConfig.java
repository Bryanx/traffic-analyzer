package be.kdg.simulator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "generator.message")
public class MessengerConfig {

    private String count;
    private String maxid;

    public int getRandomMessageCount() {
        return count != null ? Integer.parseInt(count) : 0;
    }

    public int getMaxMessageId() {
        return maxid != null ? Integer.parseInt(maxid) : 0;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setMaxid(String maxid) {
        this.maxid = maxid;
    }
}
