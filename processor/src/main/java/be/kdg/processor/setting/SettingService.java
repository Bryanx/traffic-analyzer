package be.kdg.processor.setting;

import java.util.List;

public interface SettingService {
    Setting save(Setting setting);

    Setting findByKey(String key) throws SettingNotFoundException;

    void delete(Setting setting);

    List<Setting> findAll();
}
