package be.kdg.simulator.config.converters;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class XmlConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConverter.class);
    private final XmlMapper xmlMapper = new XmlMapper();

    public String objectToXML(Object object) {
        try {
            xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            xmlMapper.registerModule(new JavaTimeModule());
            return xmlMapper.writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return "";
    }
}
