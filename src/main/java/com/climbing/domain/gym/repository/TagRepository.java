package com.climbing.domain.gym.repository;

import com.climbing.domain.gym.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByValueIn(List<String> values);
}
