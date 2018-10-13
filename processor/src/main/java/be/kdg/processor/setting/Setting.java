package be.kdg.processor.setting;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Setting {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String key;
    private String value;
}
