package be.kdg.processor.setting;

import be.kdg.processor.setting.web.SettingNotFoundException;
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
    public Setting save(Setting setting) throws SettingNotFoundException {
        switch (setting.getKey()) {
            case RETRY_DELAY_KEY: updateRetryDelay();break;
            case RETRY_COUNT_KEY: updateRetryCount();break;
        }
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

    @Override
    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    @PostConstruct
    private void updateRetryCount() throws SettingNotFoundException {
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(findByKey(RETRY_COUNT_KEY).getValue()));
    }

    @PostConstruct
    private void updateRetryDelay() throws SettingNotFoundException {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(findByKey(RETRY_DELAY_KEY).getValue());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
    }
}
