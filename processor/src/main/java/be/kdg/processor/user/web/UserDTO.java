package be.kdg.processor.user.web;

import be.kdg.processor.user.roles.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private int id;
    private String email;
    private String username;
    private String encryptedPassword;
    private List<RoleType> roleTypes;
}
