package be.kdg.processor.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {
    private Integer userId;
    private String username = null;
    private String encryptedPassword;
}
