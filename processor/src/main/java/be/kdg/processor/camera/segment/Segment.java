package be.kdg.processor.camera.segment;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Segment {

    @Id
    @GeneratedValue
    private int id;

    private int connectedCameraId;
    private int distance;
    private int speedLimit;
}