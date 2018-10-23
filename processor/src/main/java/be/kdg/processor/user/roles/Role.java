package be.kdg.processor.user.roles;

import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Entity
@DiscriminatorColumn(name = "RoleType", discriminatorType = DiscriminatorType.STRING)
public abstract class Role {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer roleId;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, insertable = false, updatable = false)
    private RoleType roleType;

    public static <T extends Role> boolean hasRole(User user, Class<T> role) throws ProcessorException {
        loadRole(user, role);
        return true;
    }

    public static <T extends Role> T loadRole(User user, Class<T> role) throws ProcessorException {
        List<Role> roles = user.getRoles();
        Optional<T> result = (Optional<T>) roles
                .stream()
                .filter(role::isInstance)
                .findAny();
        if (!result.isPresent()) throw new ProcessorException("Incorrect role for user");
        return result.get();
    }

    public static List<Role> createRoles(List<RoleType> roleTypes) {
        return roleTypes.stream().map(roleType -> new Administrator()).collect(Collectors.toList());
    }

    public abstract RoleType getRoleType();

    public abstract Collection<? extends GrantedAuthority> getAuthorities();
}
