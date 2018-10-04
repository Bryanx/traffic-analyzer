package be.kdg.processor.persistence.api;

import be.kdg.processor.domain.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Integer> {
}
