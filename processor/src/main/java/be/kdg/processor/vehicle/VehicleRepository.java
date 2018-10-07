package be.kdg.processor.vehicle;

import be.kdg.processor.camera.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
