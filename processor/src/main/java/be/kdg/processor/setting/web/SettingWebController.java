package be.kdg.processor.setting.web;

import be.kdg.processor.setting.Setting;
import be.kdg.processor.setting.SettingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SettingWebController {
    private final ModelMapper modelMapper;
    private final SettingService settingService;

    @GetMapping("/settings")
    public ModelAndView getAllSettings() {
        List<SettingDTO> settingDTOS = Arrays.asList(modelMapper.map(settingService.findAll().toArray(), SettingDTO[].class));
        SettingDTOwrapper settings = new SettingDTOwrapper();
        settings.setSettingDTOs(settingDTOS);
        return new ModelAndView("settings", "settings", settings);
    }

    @PostMapping("/settings")
    public ModelAndView updateSettings(@ModelAttribute SettingDTOwrapper settings) {
        for (SettingDTO settingDTO : settings.getSettingDTOs()) {
            settingService.save(modelMapper.map(settingDTO, Setting.class));
        }
        return new ModelAndView(new RedirectView("/settings"), "settingOut", settings);
    }
}
