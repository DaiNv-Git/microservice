package com.edu.main.function.service;

import com.edu.main.function.dto.RoomDTO;
import com.edu.main.function.entity.Room;
import javassist.NotFoundException;

import java.util.List;

public interface RoomService {

    void initRooms(List<RoomDTO> roomDTOS);

    List<Room> save(List<Room> rooms);

    Room getRoomByName(final String name) throws NotFoundException;

    List<Room> getRoomByStatus(final Boolean status, final Integer page, final Integer size);

    List<Room> getAll();
}
