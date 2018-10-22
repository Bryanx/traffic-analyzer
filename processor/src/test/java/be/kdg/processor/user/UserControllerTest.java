package be.kdg.processor.user;

import be.kdg.processor.user.web.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private User user = new User();
    private UserDTO userDTO = new UserDTO();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        user.setUsername("test");
        user.setEncryptedPassword("test");
        user = userService.save(user);
        userDTO.setUsername("test");
        userDTO.setEncryptedPassword("test");
    }

    @Test
    public void getUser() throws Exception {
        mockMvc.perform(get("/api/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("test")));
    }

    @Test
    public void create() throws Exception {
        String requestJson = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().string(containsString("test")));
    }

    @Test
    public void update() throws Exception {
        UserDTO newUser = modelMapper.map(user, UserDTO.class);
        newUser.setUsername("updatedUserName");
        String requestJson = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(put("/api/users/" + newUser.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("updatedUserName")));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(get("/api/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}