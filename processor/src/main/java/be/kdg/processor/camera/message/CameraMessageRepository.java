package be.kdg.processor.camera.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CameraMessageRepository extends JpaRepository<CameraMessage, Integer> {

    Optional<List<CameraMessage>> findAllByTimestampBetween(LocalDateTime date1, LocalDateTime date2);
}
