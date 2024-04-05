package com.climbing.api.controller;

import com.climbing.domain.gym.*;
import com.climbing.global.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @ParameterizedTest
    @ValueSource(ints = {3, 5})
    void success_get_gym_list(int length) throws Exception {
        List<Gym> gyms = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Gym gym = MockGym.of((long) i);
            gyms.add(gym);
        }
        given(gymService.findGymList()).willReturn(gyms);

        mockMvc.perform(get(BASE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(length)));
    }

    @Test
    void success_get_gym() throws Exception {
        long id = 2L;
        Gym gym = MockGym.of(2L);

        given(gymService.findGym(anyLong())).willReturn(gym);

        mockMvc.perform(get(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(gym.getName()))
                .andExpect(jsonPath("$.address.jibunAddress").value(gym.getJibunAddress()))
                .andExpect(jsonPath("$.address.roadAddress").value(gym.getRoadAddress()))
                .andExpect(jsonPath("$.address.unitAddress").value(gym.getUnitAddress()))
                .andExpect(jsonPath("$.coordinates.latitude").value(gym.getLatitude()))
                .andExpect(jsonPath("$.coordinates.longitude").value(gym.getLongitude()))
                .andExpect(jsonPath("$.description").value(gym.getDescription()))
                .andExpect(jsonPath("$.tags").value(gym.getTags()))
                .andExpect(jsonPath("$.pricing").value(gym.getPricing()))
                .andExpect(jsonPath("$.openHours").value(gym.getOpenHours()))
                .andExpect(jsonPath("$.accommodations").value(gym.getAccommodations()))
                .andExpect(jsonPath("$.contact").value(gym.getContact()))
                .andExpect(jsonPath("$.grades").value(gym.getGrades()));
    }

    @Test
    void fail_get_gym_with_not_exists_id() throws Exception {
        long id = 2L;
        given(gymService.findGym(anyLong())).willThrow(new GymException(GymExceptionType.GYM_NOT_FOUND));

        mockMvc.perform(get(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void success_post_gym() throws Exception {
        long id = 2L;
        Gym gym = MockGym.of(id);
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
    void success_delete_gym() throws Exception {
        long id = 2L;
        doNothing().when(gymService).deleteGym(anyLong());

        mockMvc.perform(delete(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void fail_delete_gym_with_not_exists_id() throws Exception {
        long id = 2L;
        doThrow(new GymException(GymExceptionType.GYM_NOT_FOUND)).when(gymService).deleteGym(anyLong());

        mockMvc.perform(delete(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isNotFound());
    }


    @Test
    void success_update_gym() throws Exception {
        long id = 2L;
        Gym gym = MockGym.of(id);

        given(gymService.updateGym(any())).willReturn(gym);

        String content = JsonUtil.toJson(gym);
        mockMvc.perform(
                        put(BASE_ENDPOINT + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(jsonPath("$.name").value(gym.getName()))
                .andExpect(jsonPath("$.address.jibunAddress").value(gym.getJibunAddress()))
                .andExpect(jsonPath("$.address.roadAddress").value(gym.getRoadAddress()))
                .andExpect(jsonPath("$.address.unitAddress").value(gym.getUnitAddress()))
                .andExpect(jsonPath("$.coordinates.latitude").value(gym.getLatitude()))
                .andExpect(jsonPath("$.coordinates.longitude").value(gym.getLongitude()))
                .andExpect(jsonPath("$.description").value(gym.getDescription()))
                .andExpect(jsonPath("$.tags").value(gym.getTags()))
                .andExpect(jsonPath("$.pricing").value(gym.getPricing()))
                .andExpect(jsonPath("$.openHours").value(gym.getOpenHours()))
                .andExpect(jsonPath("$.accommodations").value(gym.getAccommodations()))
                .andExpect(jsonPath("$.contact").value(gym.getContact()))
                .andExpect(jsonPath("$.grades").value(gym.getGrades()));
    }

    @Test
    void fail_update_gym_with_not_exists_id() throws Exception {
        long id = 2L;
        Gym gym = MockGym.of(id);

        given(gymService.updateGym(any())).willThrow(new GymException(GymExceptionType.GYM_NOT_FOUND));

        String content = JsonUtil.toJson(gym);
        mockMvc.perform(
                        put(BASE_ENDPOINT + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isNotFound());
    }
}
