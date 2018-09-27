package be.kdg.processor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer userId;

    @Column
    private String username = null;

    @Column
    private String encryptedPassword;
}
