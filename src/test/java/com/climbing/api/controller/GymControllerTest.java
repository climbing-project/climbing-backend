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

import java.lang.reflect.Field;
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

    String BASE_ENDPOINT = "/gyms";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GymService gymService;

    Gym mockGym(Long id) {
        try {
            Gym gym = Gym.of("name",
                             "address",
                             "description",
                             "tags",
                             "pricings",
                             "openHours",
                             "accommodations",
                             "contacts",
                             "grade");
            Field idField = gym.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(gym, id);
            return gym;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5})
    void getGymList(int length) throws Exception {
        List<Gym> gyms = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Gym gym = mockGym((long) i);
            gyms.add(gym);
        }
        given(gymService.findGymList()).willReturn(gyms);

        ResultActions result = mockMvc.perform(get(BASE_ENDPOINT));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(length)));
    }

    @Test
    void getGym() throws Exception {
        Long id = 2L;
        Gym gym = mockGym(2L);

        given(gymService.findGym(anyLong())).willReturn(gym);

        ResultActions result = mockMvc.perform(get(BASE_ENDPOINT + "/" + id));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(gym.getName()))
                .andExpect(jsonPath("$.address").value(gym.getAddress()))
                .andExpect(jsonPath("$.description").value(gym.getDescription()))
                .andExpect(jsonPath("$.tags").value(gym.getTags()))
                .andExpect(jsonPath("$.pricings").value(gym.getPricings()))
                .andExpect(jsonPath("$.openHours").value(gym.getOpenHours()))
                .andExpect(jsonPath("$.accommodations").value(gym.getAccommodations()))
                .andExpect(jsonPath("$.contacts").value(gym.getContacts()))
                .andExpect(jsonPath("$.grades").value(gym.getGrades()))
                .andDo(print());
    }

    @Test
    void postGym() throws Exception {
        Long id = 2L;
        Gym gym = mockGym(id);
        String content = JsonUtil.toJson(gym);

        given(gymService.createGym(any())).willReturn(id);

        mockMvc.perform(
                        post(BASE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deleteGym() throws Exception {
        long id = 2L;
        doThrow(GymException.GymNotFoundException.class).when(gymService).deleteGym(anyLong());

        mockMvc.perform(delete(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGym() throws Exception {
        long id = 2L;
        Gym gym = mockGym(id);

        given(gymService.updateGym(any())).willReturn(gym);

        String content = JsonUtil.toJson(gym);
        mockMvc.perform(
                        put(BASE_ENDPOINT + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(gym.getName()))
                .andExpect(jsonPath("$.address").value(gym.getAddress()))
                .andExpect(jsonPath("$.description").value(gym.getDescription()))
                .andExpect(jsonPath("$.tags").value(gym.getTags()))
                .andExpect(jsonPath("$.pricings").value(gym.getPricings()))
                .andExpect(jsonPath("$.openHours").value(gym.getOpenHours()))
                .andExpect(jsonPath("$.accommodations").value(gym.getAccommodations()))
                .andExpect(jsonPath("$.contacts").value(gym.getContacts()))
                .andExpect(jsonPath("$.grades").value(gym.getGrades()));
    }
}
