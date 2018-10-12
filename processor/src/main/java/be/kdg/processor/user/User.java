package be.kdg.processor.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer userId;

    @Column
    private String username = null;

    @Column
    private String encryptedPassword;
}
