package be.kdg.processor.setting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    @Override
    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public Setting findByKey(String key) throws SettingNotFoundException {
        return settingRepository.findByKey(key)
                .orElseThrow(() -> new SettingNotFoundException("Setting not found."));
    }

    @Override
    public void delete(Setting setting) {
        settingRepository.delete(setting);
    }
}
