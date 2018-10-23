package be.kdg.processor.user.web;

import be.kdg.processor.user.roles.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private int id;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String encryptedPassword;
    @JsonIgnore
    private List<RoleType> roleTypes;
}
