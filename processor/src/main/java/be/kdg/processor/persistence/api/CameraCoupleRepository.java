package be.kdg.processor.persistence.api;

import be.kdg.processor.domain.CameraCouple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraCoupleRepository extends JpaRepository<CameraCouple, Integer> {
}
