package be.kdg.processor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Fine {

    @Column(nullable = false)
    @Id
    @GeneratedValue
    private Integer fineId;

    @Column
    private String description;

}
