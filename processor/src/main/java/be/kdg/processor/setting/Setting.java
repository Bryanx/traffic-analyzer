package be.kdg.processor.setting;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Setting {
    @Id
    @GeneratedValue
    private int id;
    private String key;
    private String value;
}
