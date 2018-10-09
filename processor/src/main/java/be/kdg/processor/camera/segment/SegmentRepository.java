package be.kdg.processor.camera.segment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Integer> {
    Optional<Segment> findSegmentByConnectedCameraId(int connectedCameraId);
}
