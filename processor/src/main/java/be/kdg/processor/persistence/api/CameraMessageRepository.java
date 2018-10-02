package be.kdg.processor.persistence.api;

import be.kdg.processor.domain.CameraMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CameraMessageRepository extends JpaRepository<CameraMessage, Integer> {
}
