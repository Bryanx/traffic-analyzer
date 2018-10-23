package be.kdg.processor.setting;

import be.kdg.processor.shared.exception.ProcessorException;

import java.util.List;

public interface SettingService {
    Setting save(Setting setting) throws ProcessorException;

    Setting findByKey(String key) throws ProcessorException;

    void delete(Setting setting);

    List<Setting> findAll();

    Setting updateSetting(String key, int value) throws ProcessorException;
}
