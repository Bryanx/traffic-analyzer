package be.kdg.processor.user.roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findFirstByRoleType(RoleType type);
}
