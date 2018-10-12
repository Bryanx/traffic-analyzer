package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GeneralFineService implements FineService {
    private final FineRepository fineRepository;

    @Override
    public Fine save(Fine fine) {
        return fineRepository.save(fine);
    }

    @Override
    public Fine findById(int id) throws FineException {
        return fineRepository.findById(id)
                .orElseThrow(() -> new FineException("Fine not found"));
    }

    @Override
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }

    @Override
    public List<Fine> findAllByVehicleIn(Vehicle vehicle) {
        return fineRepository.findAllByVehicleIn(vehicle);
    }

    @Override
    public List<Fine> findAllByCreationDateBetween(LocalDateTime start, LocalDateTime end) {
        return fineRepository.findAllByCreationDateBetween(start, end);
    }
}
