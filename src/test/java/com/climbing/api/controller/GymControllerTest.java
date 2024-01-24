package com.climbing.api.controller;

import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymException;
import com.climbing.domain.gym.GymService;
import com.climbing.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GymControllerTest {

    static String BASE_ENDPOINT = "/gyms";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GymService gymService;

    @ParameterizedTest
    @ValueSource(ints = {3, 5})
    void getGymList(int length) throws Exception {
        List<Gym> gyms = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Gym gym = Gym.of((long) i, "name", "address");
            gyms.add(gym);
        }
        given(gymService.findGymList()).willReturn(gyms);

        ResultActions result = mockMvc.perform(get(BASE_ENDPOINT));

        result.andExpect(jsonPath("$", hasSize(length)));
    }

    @Test
    void getGym() throws Exception {
        Long id = 2L;
        String name = "name";
        String address = "address";
        Gym gym = Gym.of(id, name, address);

        given(gymService.findGym(anyLong())).willReturn(gym);

        ResultActions result = mockMvc.perform(get(BASE_ENDPOINT + "/" + id));

        result.andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.address").value(address))
                .andDo(print());
    }

    @Test
    void postGym() throws Exception {
        Long id = 2L;
        String name = "name";
        String address = "address";
        Gym gym = Gym.of(id, name, address);
        String content = JsonUtil.toJson(gym);

        given(gymService.createGym(any())).willReturn(id);

        mockMvc.perform(
                        post(BASE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deleteGym() throws Exception {
        long id = 2L;

        doThrow(GymException.GymNotFoundException.class).when(gymService).deleteGym(anyLong());

        mockMvc.perform(delete(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isBadRequest());
    }
}
