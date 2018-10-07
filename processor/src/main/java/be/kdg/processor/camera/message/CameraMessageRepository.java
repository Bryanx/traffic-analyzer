package be.kdg.processor.camera.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CameraMessageRepository extends JpaRepository<CameraMessage, Integer> {

    List<CameraMessage> findAllByTimestampBetween(LocalDateTime date1, LocalDateTime date2);
}
