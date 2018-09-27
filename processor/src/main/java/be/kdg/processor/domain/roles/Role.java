package be.kdg.processor.domain.roles;

import be.kdg.processor.domain.User;

import javax.persistence.*;

@Entity
public abstract class Role {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer roleId;

    @ManyToOne(targetEntity = User.class)
    private User user;
}