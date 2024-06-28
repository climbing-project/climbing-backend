package com.climbing.domain.gym.repository;

import com.climbing.domain.gym.Gym;
import com.climbing.domain.member.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
//    List<Gym> findByIdIn(List<Long> ids);
    Page<Gym> findAllByJibunAddressStartsWith(String address, Pageable pageable);

    Page<Gym> findAllByNameContains(String name, Pageable pageable);

    List<Gym> findAllBy(Pageable pageable);

    List<Gym> findAllByMember(Member member);

//    List<Gym> findAllByOrderByCreatedAtDesc(String address);
//    List<Gym> findAllByAddressOrderByCreatedAtDesc(String address);
//    List<Gym> findAllByAddressOrderByNameAsc(String address);
//    List<Gym> findAllByAddressOrderByHitsDesc(String address);

}
