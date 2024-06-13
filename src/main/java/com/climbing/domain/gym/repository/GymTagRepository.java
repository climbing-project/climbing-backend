package com.climbing.domain.gym.repository;

import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymTag;
import com.climbing.domain.gym.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymTagRepository extends JpaRepository<GymTag, Long> {
    List<GymTag> findByTagIn(List<Tag> tags);
    void deleteAllByGym(Gym gym);
}
