package com.climbing.domain.gym;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
//    List<Gym> findByIdIn(List<Long> ids);
    List<Gym> findByIdInOrderByHitsDesc(List<Long> range);

    List<Gym> findByIdInOrderByCreatedAtDesc(List<Long> range);

    List<Gym> findByIdIn(List<Long> range);

    List<Gym> findByIdInOrderByNameAsc(List<Long> range);


//    List<Gym> findAllByOrderByCreatedAtDesc(String address);
//    List<Gym> findAllByAddressOrderByCreatedAtDesc(String address);
//    List<Gym> findAllByAddressOrderByNameAsc(String address);
//    List<Gym> findAllByAddressOrderByHitsDesc(String address);

}
