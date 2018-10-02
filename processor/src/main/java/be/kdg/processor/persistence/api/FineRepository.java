package be.kdg.processor.persistence.api;

import be.kdg.processor.domain.CameraMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<CameraMessage, Integer> {
}
