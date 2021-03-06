package be.kdg.processor.setting.web;

import be.kdg.processor.setting.Setting;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.exception.ProcessorException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SettingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);
    private final SettingService settingService;
    private final ModelMapper modelMapper;

    @GetMapping("/settings/{key}")
    public ResponseEntity<SettingDTO> getSetting(@PathVariable String key) throws ProcessorException {
        Setting setting = settingService.findByKey(key);
        return new ResponseEntity<>(modelMapper.map(setting, SettingDTO.class), HttpStatus.OK);
    }

    @PatchMapping("/settings/{key}")
    public ResponseEntity<SettingDTO> updateSetting(@PathVariable String key, @RequestBody SettingDTO settingDTO) throws ProcessorException {
        Setting settingOut= settingService.updateSetting(key, settingDTO.getValue());
        return new ResponseEntity<>(modelMapper.map(settingOut, SettingDTO.class), HttpStatus.OK);
    }
}
