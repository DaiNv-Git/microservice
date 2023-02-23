package com.edu.main.function.service.impl;

import com.edu.main.function.dto.RoomDTO;
import com.edu.main.function.entity.Room;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.RoomRepository;
import com.edu.main.function.service.RoomService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> save(List<Room> rooms) {
        if (CollectionUtils.isEmpty(rooms)) {
            throw new IllegalArgumentException("List room can not be null");
        }
        rooms.stream().forEach(r -> {
            if (roomRepository.findByName(r.getName()) != null) {
                throw new IllegalArgumentException("Name " + r.getName() + " already exist");
            }
        });
        return roomRepository.saveAll(rooms);
    }

    @Override
    public void initRooms(List<RoomDTO> roomDTOS) {
        if (!CollectionUtils.isEmpty(roomDTOS)) {
            List<Room> rooms = dtoMapper.toRoom(roomDTOS);
            if (!CollectionUtils.isEmpty(rooms)) {
                rooms.stream().forEach(r -> r.setStatus(Boolean.TRUE));
                this.save(rooms);
            }
        }
    }

    @Override
    public Room getRoomByName(final String name) throws NotFoundException {
        Room room = roomRepository.findByName(name);
        if (room == null) {
            throw new NotFoundException("Room not found");
        }
        return room;
    }

    @Override
    public List<Room> getRoomByStatus(final Boolean status, final Integer page, final Integer size) {
        List<Room> rooms = roomRepository.findAll();
        if (status != null) {
            rooms = roomRepository.findByStatus(status);
        }
        PagedListHolder pagedListHolder = new PagedListHolder(rooms);
        pagedListHolder.setPage(page != null ? page : 0);
        pagedListHolder.setPageSize(size != null ? size : 4);
        return pagedListHolder.getPageList();
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }
}
