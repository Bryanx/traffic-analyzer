package be.kdg.processor.fine;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FineController {

    private final FineService fineService;
    private final ModelMapper modelMapper;

    @GetMapping("/fines/{id}")
    public ResponseEntity<FineDTO> getFine(@PathVariable int id) throws FineException {
        Fine fine = fineService.findById(id);
        return new ResponseEntity<>(modelMapper.map(fine, FineDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/fines", params = { "from", "to" }, method = GET)
    public ResponseEntity<FineDTO[]> getFilteredFines(@RequestParam(value = "from") Optional<String> from,
                                                      @RequestParam(value = "to") Optional<String> to) {
        List<Fine> fines;
//        if (!from.isPresent() && !to.isPresent()) fines = fineService.findAll();
//        else if (!from.isPresent()) fines = fineService.findAllByCreationDateBetween(LocalDateTime.MIN, to.get());
//        else if (!to.isPresent()) fines = fineService.findAllByCreationDateBetween(from.get(), LocalDateTime.MAX);
//        else fines = fineService.findAllByCreationDateBetween(from.get(), to.get());
//
//        return new ResponseEntity<>(modelMapper.map(fines.toArray(), FineDTO[].class), HttpStatus.OK);
        return null;
    }

    @PostMapping("/fines")
    public ResponseEntity<FineDTO> createFine(@RequestBody FineDTO fineDTO) {
        Fine fineIn = modelMapper.map(fineDTO, Fine.class);
        Fine fineOut = fineService.save(fineIn);
        return new ResponseEntity<>(modelMapper.map(fineOut, FineDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/fines/{id}/approve")
    public ResponseEntity<FineDTO> toggleApproveFine(@PathVariable int id) throws FineException {
        Fine fineOut = fineService.findById(id);
        fineOut.setApproved(!fineOut.isApproved());
        return new ResponseEntity<>(modelMapper.map(fineOut, FineDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/fines/{id}")
    public void deleteFine(@PathVariable int id) {
        fineService.deleteById(id);
    }
}
