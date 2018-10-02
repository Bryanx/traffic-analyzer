package be.kdg.processor.persistence.api;

import be.kdg.processor.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraRepository extends JpaRepository<Camera, Integer> {
}
