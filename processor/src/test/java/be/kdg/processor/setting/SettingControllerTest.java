package be.kdg.processor.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SettingControllerTest {

    public static final SettingDTO SETTING_DTO = new SettingDTO("test", 10, "test description");
    public static final Setting SETTING = new Setting("test", 5);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getSetting() throws Exception {
        Setting setting = settingService.save(SETTING);
        mockMvc.perform(get("/api/settings/" + setting.getKey())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("test")));
    }

    @Test
    public void updateSetting() throws Exception {
        Setting setting = settingService.save(SETTING);
        String requestJson = objectMapper.writeValueAsString(SETTING_DTO);
        mockMvc.perform(put("/api/settings/" + setting.getKey() )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("10")));
    }
}