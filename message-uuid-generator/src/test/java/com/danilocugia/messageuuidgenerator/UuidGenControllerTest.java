package com.danilocugia.messageuuidgenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UuidGenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnUuidString() throws Exception {

        MvcResult result = mockMvc.perform(get("/message-uuid"))
                .andExpect(status().isOk())
                .andReturn();
        String uuid = result.getResponse().getContentAsString();
        assertTrue(uuid.matches("pre-.*-suf"));
    }

}
