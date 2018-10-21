package be.kdg.processor.fine.web;

import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @PostMapping("/fines/{id}")
    public ModelAndView updateFine(@PathVariable int id, @ModelAttribute FineDTO fineDTO) throws FineException {
        Fine fine = fineService.findById(id);
        fine.setApproved(fineDTO.isApproved());
        fine.setComment(fineDTO.getComment());
        fine.setPrice(fineDTO.getPrice());
        Fine fineOut = fineService.save(fine);
        return new ModelAndView(new RedirectView("/fines"), "fineOut", fineOut);
    }
}
