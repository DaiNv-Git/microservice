package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.RequestBookingRoomDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.RequestStatus;
import com.edu.main.function.entity.RequestBookingRoom;
import com.edu.main.function.entity.Room;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.RequestBookingRoomRepository;
import com.edu.main.function.service.RequestBookingRoomService;
import com.edu.main.function.service.RoomService;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestBookingRoomServiceImpl implements RequestBookingRoomService {

    @Autowired
    private RequestBookingRoomRepository requestRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private AuthClients authClients;

    @Override
    public RequestBookingRoom getById(final Long id) throws NotFoundException {
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Request not found"));
    }

    @Override
    public RequestBookingRoom createRequest(RequestBookingRoomDTO requestDTO) throws NotFoundException {
        RequestBookingRoom request = dtoMapper.toRequestBookingRoom(requestDTO);
        Room room = roomService.getRoomByName(requestDTO.getRoomName());
        if (room == null) {
            throw new NotFoundException("Room not found");
        }
        if (!room.getStatus()) {
            throw new IllegalArgumentException("Room unavailable");
        }
        request.setRoom(room);
        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    @Override
    public RequestBookingRoom updateRequest(final Long id, RequestBookingRoomDTO requestDTO) throws NotFoundException {
        RequestBookingRoom request = this.getById(id);
        if (!StringUtils.isBlank(requestDTO.getDescription())) {
            request.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getStartTime() != null) {
            request.setStartTime(requestDTO.getStartTime());
        }
        if (requestDTO.getEndTime() != null) {
            request.setEndTime(requestDTO.getEndTime());
        }
        if (requestDTO.getStatus() != null) {
            RequestStatus requestStatus = dtoMapper.convertToRequestStatus(requestDTO.getStatus());
            request.setStatus(requestStatus);
        }
        if (!StringUtils.isBlank(requestDTO.getRoomName())
                && !requestDTO.getRoomName().equalsIgnoreCase(request.getRoom().getName())) {
            Room room = roomService.getRoomByName(requestDTO.getRoomName());
            if (room == null) {
                throw new NotFoundException("Room not found");
            }
            if (!room.getStatus()) {
                throw new IllegalArgumentException("Room unavailable");
            }
            request.setRoom(room);
        }
        return requestRepository.save(request);
    }

    @Override
    public RequestBookingRoom changeStatusRequestForAdmin(final Long id, final String status)
            throws NotFoundException {
        RequestStatus requestStatus = dtoMapper.convertToRequestStatus(status);
        if (status == null) {
            throw new IllegalArgumentException("Status can not be null");
        }
        RequestBookingRoom request = this.getById(id);
        request.setStatus(requestStatus);
        return requestRepository.save(request);
    }

    @Override
    public List<RequestBookingRoom> getRequestByStatusOfCurrentUser(String status, Integer page, Integer size) {
        UserAppDTO currentUser = authClients.getCurrentUser();
        List<RequestBookingRoom> requestBookingRooms = requestRepository.findByUsername(currentUser.getUsername());
        if (status != null) {
            RequestStatus requestStatus = dtoMapper.convertToRequestStatus(status);
            requestBookingRooms = requestRepository.findByUsernameAndStatus(currentUser.getUsername(), requestStatus);
        }
        PagedListHolder pagedListHolder = new PagedListHolder(requestBookingRooms);
        pagedListHolder.setPage(page != null ? page : 0);
        pagedListHolder.setPageSize(size != null ? size : 4);
        return pagedListHolder.getPageList();
    }

    @Override
    public List<RequestBookingRoom> getRequestByStatus(String username, String status, Integer page, Integer size) {
        List<RequestBookingRoom> requestBookingRooms = requestRepository.findAll();
        if (!StringUtils.isBlank(username)) {
            if (!StringUtils.isBlank(status)) {
                RequestStatus requestStatus = dtoMapper.convertToRequestStatus(status);
                requestBookingRooms = requestRepository.findByUsernameAndStatus(username, requestStatus);
            } else {
                requestBookingRooms = requestRepository.findByUsername(username);
            }
        } else {
            if (!StringUtils.isBlank(status)) {
                RequestStatus requestStatus = dtoMapper.convertToRequestStatus(status);
                requestBookingRooms = requestRepository.findByStatus(requestStatus);
            }
        }
        PagedListHolder pagedListHolder = new PagedListHolder(requestBookingRooms);
        pagedListHolder.setPage(page != null ? page : 0);
        pagedListHolder.setPageSize(size != null ? size : 4);
        return pagedListHolder.getPageList();
    }
}
