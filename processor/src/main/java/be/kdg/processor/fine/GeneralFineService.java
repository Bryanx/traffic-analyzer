package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GeneralFineService implements FineService {
    private final FineRepository fineRepository;

    @Override
    public Fine saveAndFlush(Fine fine) {
        return fineRepository.saveAndFlush(fine);
    }

    @Override
    public Fine findById(int id) throws FineException {
        return fineRepository.findById(id)
                .orElseThrow(() -> new FineException("Greeting not found"));
    }

    @Override
    public List<Fine> findAllByVehicleIn(Vehicle vehicle) {
        return fineRepository.findAllByVehicleIn(vehicle);
    }

}
