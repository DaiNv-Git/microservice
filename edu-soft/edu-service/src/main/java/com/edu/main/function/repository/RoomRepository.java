package com.edu.main.function.repository;

import com.edu.main.function.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByName(final String name);

    List<Room> findByStatus(final Boolean status);
}
