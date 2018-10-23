package be.kdg.processor.fine;

import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class FineServiceImpl implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FineServiceImpl.class);
    private static final String EMISSION_DELAY_KEY = "emission-fine-delay";
    private final FineRepository fineRepository;
    private final SettingService settingService;
    private final DateUtil dateUtil;

    @Override
    public Fine save(Fine fine) {
        return fineRepository.save(fine);
    }

    @Override
    public Fine findById(int id) throws ProcessorException {
        return fineRepository.findById(id)
                .orElseThrow(() -> new ProcessorException("Fine not found"));
    }

    @Override
    public Fine updatePrice(int id, double price, String comment) throws ProcessorException {
        Fine fine = findById(id);
        fine.setPrice(price);
        fine.setComment(comment);
        return save(fine);
    }

    @Override
    public Fine updateApproved(int id, boolean approved) throws ProcessorException {
        Fine fine = findById(id);
        fine.setApproved(approved);
        return save(fine);
    }

    @Override
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }

    @Override
    public List<Fine> findAllByTypeAndVehicle(FineType fineType, Vehicle vehicle) {
        return fineRepository.findAllByTypeEqualsAndVehicleIn(fineType, vehicle);
    }

    @Override
    public List<Fine> findAllByCreationDateBetween(Optional<LocalDateTime> start, Optional<LocalDateTime> end) {
        LocalDateTime fromDate = start.orElse(LocalDateTime.MIN);
        LocalDateTime toDate = end.orElse(LocalDateTime.MAX);
        return fineRepository.findAllByCreationDateBetween(fromDate, toDate);
    }

    /**
     * Checks if there is a fine that was created in the emission-fine-delay time period.
     *
     * @param vehicle
     * @return
     */
    @Override
    public boolean checkIfAlreadyHasEmissionFine(Vehicle vehicle) throws ProcessorException {
        double emissionDelay = settingService.findByKey(EMISSION_DELAY_KEY).getValue();
        return findAllByTypeAndVehicle(FineType.EMISSION, vehicle).stream()
                .mapToDouble(fine -> dateUtil.getHoursBetweenDates(LocalDateTime.now(), fine.getCreationDate()))
                .anyMatch(hoursSinceFine -> hoursSinceFine < emissionDelay);
    }

}
