package com.edu.main.function.service;

import com.edu.main.function.dto.RequestBookingRoomDTO;
import com.edu.main.function.entity.RequestBookingRoom;
import javassist.NotFoundException;

import java.util.List;

public interface RequestBookingRoomService {

    RequestBookingRoom getById(final Long id) throws NotFoundException;

    RequestBookingRoom createRequest(RequestBookingRoomDTO requestDTO) throws NotFoundException;

    RequestBookingRoom updateRequest(final Long id, RequestBookingRoomDTO requestDTO) throws NotFoundException;

    RequestBookingRoom changeStatusRequestForAdmin(final Long id, final String status) throws NotFoundException;

    List<RequestBookingRoom> getRequestByStatusOfCurrentUser(final String status, final Integer page, final Integer size);

    List<RequestBookingRoom> getRequestByStatus(final String username, final String status, final Integer page, final Integer size);
}
