package be.kdg.processor.camera.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue
    private int cameraId;

    @JsonProperty(value = "lat")
    private double latitude;

    @JsonProperty(value = "long")
    private double longitude;
}
