package be.kdg.processor.setting.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SettingDTO {
    private String key;
    private int value;
    private String description;
}
