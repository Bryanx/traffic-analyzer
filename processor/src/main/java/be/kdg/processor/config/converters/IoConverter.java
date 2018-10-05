package be.kdg.processor.config.converters;

import be.kdg.processor.domain.CameraCouple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public <T> T jsonToObject(String json, Class<T> instance) {
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

    public JsonObject readJson(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public CameraCouple jsonToObjects(JsonObject jsonObject) {
        CameraCouple couple;
        JsonObject segment = jsonObject.getAsJsonObject("segment");
        if (segment != null) {
            couple = new CameraCouple(segment);
            return couple;
        }
        return null;
    }
}
