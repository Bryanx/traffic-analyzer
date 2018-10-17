package be.kdg.processor.setting;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class Setting {
    @Id
    private String key;
    private int value;
    private String description;

    public Setting(String key, int value) {
        this.key = key;
        this.value = value;
    }
}
