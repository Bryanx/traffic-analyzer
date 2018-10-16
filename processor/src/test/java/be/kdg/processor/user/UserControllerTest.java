package be.kdg.processor.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Test
    public void getSetting() {
        //TODO
    }

    @Test
    public void create() {
        //TODO
    }

    @Test
    public void update() {
        //TODO
    }

    @Test
    public void delete() {
        //TODO
    }
}