package be.kdg.processor.setting;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/setting")
public class SettingWebController {
    private final ModelMapper modelMapper;
    private final SettingService settingService;

    @GetMapping("/setting.do")
    public ModelAndView showSettingForm(SettingDTO settingDTO) {
        return new ModelAndView("settingForm", "settingDTO", settingDTO);
    }

    @PostMapping("/newsetting.do")
    public ModelAndView createSetting(@Valid @ModelAttribute SettingDTO settingDTO) {
        Setting setting = settingService.save(modelMapper.map(settingDTO, Setting.class));
        //TODO: redirect url to prevent refresh create post
        return new ModelAndView("settingOverview", "settingDTO", modelMapper.map(setting, SettingDTO.class));
    }
}
