package be.kdg.processor.config.converters;

import be.kdg.processor.domain.CameraMessage;
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
public class XmlConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConverter.class);
    private final XmlMapper mapper = new XmlMapper();

    public CameraMessage xmlToCameraMessage(String xml) {
        try {
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(xml, CameraMessage.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }
}
