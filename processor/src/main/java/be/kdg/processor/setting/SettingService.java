package be.kdg.processor.setting;

import be.kdg.processor.setting.web.SettingNotFoundException;

import java.util.List;

public interface SettingService {
    Setting save(Setting setting);

    Setting findByKey(String key) throws SettingNotFoundException;

    void delete(Setting setting);

    List<Setting> findAll();
}
