package be.kdg.processor.fine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FineController.class);
    private final FineService fineService;
    private final ModelMapper modelMapper;

    @GetMapping("/fines/{id}")
    public ResponseEntity<FineDTO> getFine(@PathVariable int id) throws FineException {
        LOGGER.info("GET request for fine: {}", id);
        Fine fine = fineService.findById(id);
        return new ResponseEntity<>(modelMapper.map(fine, FineDTO.class), HttpStatus.OK);
    }

    @GetMapping("/fines")
    public ResponseEntity<FineDTO[]> getFines() {
        LOGGER.info("GET request for all fines: {}");
        List<Fine> fines = fineService.findAll();
        return new ResponseEntity<>(modelMapper.map(fines.toArray(), FineDTO[].class), HttpStatus.OK);
    }

    @RequestMapping(value = "/fines", params = {"from", "to"}, method = GET)
    public ResponseEntity<FineDTO[]> getFilteredFines(
            @RequestParam(value = "from") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> from,
            @RequestParam(value = "to") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> to) {
        LocalDateTime fromDate = from.orElse(LocalDateTime.MIN);
        LocalDateTime toDate = to.orElse(LocalDateTime.MAX);
        List<Fine> fines = fineService.findAllByCreationDateBetween(fromDate, toDate);
        return new ResponseEntity<>(modelMapper.map(fines.toArray(), FineDTO[].class), HttpStatus.OK);
    }

    @PostMapping("/fines")
    public ResponseEntity<FineDTO> createFine(@RequestBody FineDTO fineDTO) {
        Fine fineIn = modelMapper.map(fineDTO, Fine.class);
        Fine fineOut = fineService.save(fineIn);
        return new ResponseEntity<>(modelMapper.map(fineOut, FineDTO.class), HttpStatus.CREATED);
    }

    @PatchMapping("/fines/{id}")
    public ResponseEntity<FineDTO> updateFine(@PathVariable int id, @RequestBody FineDTO fineIn) throws FineException {
        Fine fineOut = fineService.findById(id);
        fineOut.setComment(fineIn.getComment());
        fineOut.setPrice(fineIn.getPrice());
        fineOut.setApproved(fineIn.isApproved());
        return new ResponseEntity<>(modelMapper.map(fineOut, FineDTO.class), HttpStatus.OK);
    }

}
