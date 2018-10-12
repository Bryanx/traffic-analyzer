package be.kdg.processor.setting;

public interface SettingService {
    Setting save(Setting setting);

    Setting findByKey(String key) throws SettingNotFoundException;

    void delete(Setting setting);
}
