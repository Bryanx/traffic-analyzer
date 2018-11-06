package be.kdg.processor.fine;

import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FineService {
    Fine save(Fine fine);

    Fine findById(int id) throws ProcessorException;

    Fine updatePrice(int id, double price, String comment) throws ProcessorException;

    Fine updateApproved(int id, boolean approved) throws ProcessorException;

    List<Fine> findAll();

    List<Fine> findAllByTypeAndVehicle(FineType fineType, Vehicle vehicle);

    List<Fine> findAllByCreationDateBetween(Optional<LocalDateTime> start, Optional<LocalDateTime> end);

    boolean checkIfAlreadyHasEmissionFine(Vehicle vehicle) throws ProcessorException;

    double getAverageFinePrice();
}
