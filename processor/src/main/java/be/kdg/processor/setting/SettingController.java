package be.kdg.processor.setting;

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
    public ResponseEntity<SettingDTO> getSetting(@PathVariable String key) throws SettingNotFoundException {
        Setting setting = settingService.findByKey(key);
        return new ResponseEntity<>(modelMapper.map(setting, SettingDTO.class), HttpStatus.OK);
    }

    @PutMapping("/settings/{key}")
    public ResponseEntity<SettingDTO> updateSetting(@PathVariable String key, @RequestBody int value) throws SettingNotFoundException {
        Setting setting = settingService.findByKey(key);
        setting.setValue(value);
        Setting settingOut = settingService.save(setting);
        return new ResponseEntity<>(modelMapper.map(settingOut, SettingDTO.class), HttpStatus.OK);
    }
}
