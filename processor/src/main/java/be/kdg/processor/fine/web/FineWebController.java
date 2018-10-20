package be.kdg.processor.fine.web;

import be.kdg.processor.fine.FineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class FineWebController {
    private final ModelMapper modelMapper;
    private final FineService fineService;

    @GetMapping("/fines")
    public ModelAndView getAllFines() {
        FineDTO[] fines = modelMapper.map(fineService.findAll().toArray(), FineDTO[].class);
        return new ModelAndView("fines", "fines", fines);
    }

    @GetMapping("/fines/details/{id}")
    public ModelAndView GetFine(@PathVariable int id) throws FineException {
        FineDTO fine = modelMapper.map(fineService.findById(id), FineDTO.class);
        return new ModelAndView("fines/details", "fine", fine);
    }
}
