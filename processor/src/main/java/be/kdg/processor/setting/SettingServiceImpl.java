package be.kdg.processor.setting;

import be.kdg.processor.shared.exception.ProcessorException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SettingServiceImpl implements SettingService {
    private static final String RETRY_DELAY_KEY = "message-retry-delay";
    private static final String RETRY_COUNT_KEY = "message-retry-count";
    private final SettingRepository settingRepository;
    private final RetryTemplate retryTemplate;

    @Override
    public Setting save(Setting setting) throws ProcessorException {
        switch (setting.getKey()) {
            case RETRY_DELAY_KEY: updateRetryDelay();break;
            case RETRY_COUNT_KEY: updateRetryCount();break;
        }
        return settingRepository.save(setting);
    }

    @Override
    public Setting findByKey(String key) throws ProcessorException {
        return settingRepository.findByKey(key)
                .orElseThrow(() -> new ProcessorException("Setting not found."));
    }

    @Override
    public void delete(Setting setting) {
        settingRepository.delete(setting);
    }

    @Override
    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    @Override
    public Setting updateSetting(String key, int value) throws ProcessorException {
        Setting setting = findByKey(key);
        setting.setValue(value);
        return setting;
    }

    @PostConstruct
    private void updateRetryCount() throws ProcessorException {
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(findByKey(RETRY_COUNT_KEY).getValue()));
    }

    @PostConstruct
    private void updateRetryDelay() throws ProcessorException {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(findByKey(RETRY_DELAY_KEY).getValue());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
    }
}
