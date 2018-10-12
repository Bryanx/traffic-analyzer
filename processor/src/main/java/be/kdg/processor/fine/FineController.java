package be.kdg.processor.fine;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/fines")
    public ResponseEntity<FineDTO> createFine(@RequestBody FineDTO fineDTO) {
        Fine fineIn = modelMapper.map(fineDTO, Fine.class);
        Fine fineOut = fineService.saveAndFlush(fineIn);
        return new ResponseEntity<>(modelMapper.map(fineOut, FineDTO.class), HttpStatus.CREATED);
    }
}
