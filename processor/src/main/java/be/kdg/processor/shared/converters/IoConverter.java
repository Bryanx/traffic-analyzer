package be.kdg.processor.shared.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class IoConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IoConverter.class);
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    public <T> T readJson(String json, Class<T> instance) {
        try {
            return objectMapper.readValue(json, instance);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> T readXml(String xml, Class<T> instance) {
        try {
            xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            xmlMapper.registerModule(new JavaTimeModule());
            return xmlMapper.readValue(xml, instance);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
