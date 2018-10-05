package be.kdg.processor.shared.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class IoConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IoConverter.class);
    private final ObjectMapper objectMapper;

    public <T> T readJson(String json, Class<T> instance) {
        try {
            return objectMapper.readValue(json, instance);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
