package be.kdg.processor.fine.web;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.shared.livestats.LiveStatsController;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
public class FineWebController extends LiveStatsController {
    private final ModelMapper modelMapper;

    public FineWebController(FineService fineService, CameraService cameraService, ModelMapper modelMapper, FineService fineService1) {
        super(fineService, cameraService);
        this.modelMapper = modelMapper;
    }

    @GetMapping("/fines")
    public ModelAndView getAllFines() {
        FineDTO[] fines = modelMapper.map(fineService.findAll().toArray(), FineDTO[].class);
        return new ModelAndView("fines", "fines", fines);
    }

    @GetMapping("/fines/details/{id}")
    public ModelAndView GetFine(@PathVariable int id) throws ProcessorException {
        FineDTO fine = modelMapper.map(fineService.findById(id), FineDTO.class);
        return new ModelAndView("fines/details", "fine", fine);
    }

    @PostMapping("/saveFine")
    public ModelAndView updateFine(@Valid @ModelAttribute FineDTO fineDTO, BindingResult errors) throws ProcessorException {
        if (!errors.hasErrors()) {
            Fine fine = fineService.findById(fineDTO.getId());
            fine.setApproved(fineDTO.isApproved());
            fine.setComment(fineDTO.getComment());
            fine.setPrice(fineDTO.getPrice());
            Fine fineOut = fineService.save(fine);
            return new ModelAndView(new RedirectView("/fines"), "fineOut", fineOut);
        }
        return new ModelAndView(new RedirectView("/fines/details/"+fineDTO.getId()));
    }
}
