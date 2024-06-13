package com.climbing.domain.gym.repository;

import com.climbing.domain.gym.GymTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymTagRepository extends JpaRepository<GymTag, Long> {
}
