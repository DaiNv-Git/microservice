package com.edu.main.function.repository;

import com.edu.main.function.dto.enums.RequestStatus;
import com.edu.main.function.entity.RequestBookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestBookingRoomRepository extends JpaRepository<RequestBookingRoom, Long> {

    List<RequestBookingRoom> findByStatus(RequestStatus requestStatus);

    List<RequestBookingRoom> findByUsername(final String username);

    List<RequestBookingRoom> findByUsernameAndStatus(String username, RequestStatus status);
}
