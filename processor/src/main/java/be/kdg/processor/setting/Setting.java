package be.kdg.processor.setting;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Setting {
    @Id
    private String key;
    private int value;
}
