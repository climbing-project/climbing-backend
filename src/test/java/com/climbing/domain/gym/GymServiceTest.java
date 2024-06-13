package com.climbing.domain.gym;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.api.request.MockPostGymRequest;
import com.climbing.api.request.MockUpdateGymRequest;
import com.climbing.domain.gym.repository.GymRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class GymServiceTest {

    @InjectMocks
    GymService gymService;

    @Mock
    GymRepository gymRepository;

    @Test
    void success_find_gym_list() {
        List<Gym> mockGymList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mockGymList.add(MockGym.of((long) i));
        }

        given(gymRepository.findAll()).willReturn(mockGymList);

        List<Gym> gymList = gymService.findGymList();

        assertThat(gymList).usingRecursiveComparison().isEqualTo(mockGymList);
    }

    @Test
    void success_find_gym_by_id() {
        long id = 2L;
        Gym mockGym = MockGym.of(id);
        given(gymRepository.findById(anyLong())).willReturn(Optional.of(mockGym));

        Gym gym = gymService.findGym(id);

        assertThat(gym).isEqualTo(mockGym);
    }

    @Test
    void success_create_gym() {
        Long mockId = 2L;
        given(gymRepository.save(any())).willReturn(MockGym.of(mockId));

        PostGymCommand command = MockPostGymRequest.of().toCommand();
        Long id = gymService.createGym(command);

        assertThat(id).isEqualTo(mockId);
    }

    @Test
    void success_delete_gym_by_id() {
        Long mockId = 2L;
        given(gymRepository.existsById(anyLong())).willReturn(true);
        doNothing().when(gymRepository).deleteById(anyLong());

        assertThatNoException().isThrownBy(() -> gymService.deleteGym(mockId));
    }

    @Test
    void fail_delete_gym_with_not_exists_id() {
        Long mockId = 2L;
        given(gymRepository.existsById(anyLong())).willReturn(false);

        assertThatException().isThrownBy(() -> gymService.deleteGym(mockId))
                .isInstanceOfSatisfying(GymException.class,
                                        (e) -> assertThat(e.getExceptionType()).isEqualTo(GymExceptionType.GYM_NOT_FOUND));
    }

    @Test
    void success_update_gym() {
        Long id = 2L;
        Gym mockGym = MockGym.of(id);
        given(gymRepository.findById(any())).willReturn(Optional.of(mockGym));
        given(gymRepository.save(any())).willReturn(mockGym);

        UpdateGymCommand command = MockUpdateGymRequest.of().toCommand(id);
        Gym gym = gymService.updateGym(command);

        assertThat(gym).isEqualTo(mockGym);
    }
}
