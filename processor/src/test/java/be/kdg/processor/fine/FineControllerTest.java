package be.kdg.processor.fine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FineControllerTest {

    public static final FineDTO FINE_DTO = new FineDTO(1,FineType.EMISSION, 10.0, 2, 1);
    public static final Fine FINE = new Fine(FineType.EMISSION, 10.0, 2, 1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FineService fineService;

    @Test
    public void createFine() throws Exception {
        String requestJson = objectMapper.writeValueAsString(FINE_DTO);
        mockMvc.perform(post("/api/fines")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().string(containsString("EMISSION")));
    }

    @Test
    public void getFilteredFines() throws Exception {
        List<Fine> fines = Arrays.asList(fineService.save(FINE), fineService.save(FINE), fineService.save(FINE));
        fines.forEach(fine -> fine.setCreationDate(LocalDateTime.MIN));
        fines.forEach(fine -> fineService.save(fine));

        mockMvc.perform(get(String.format("/api/fines?from=%s&to=%s", LocalDateTime.now().minusDays(1), LocalDateTime.now()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(nullValue()));

        fines.forEach(fine -> fine.setCreationDate(LocalDateTime.now()));
        fines.forEach(fine -> fineService.save(fine));
        mockMvc.perform(get(String.format("/api/fines?from=%s&to=%s", LocalDateTime.now().minusDays(1), LocalDateTime.now()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void getFine() throws Exception {
        Fine fine = fineService.save(FINE);
        Integer id = fine.getId();
        mockMvc.perform(get("/api/fines/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("EMISSION")));
    }

    @Test
    public void toggleApproveFine() throws Exception {
        Fine fine = fineService.save(FINE);
        Integer id = fine.getId();
        mockMvc.perform(put("/api/fines/" + id + "/approve")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("\"approved\":true")));
    }
}