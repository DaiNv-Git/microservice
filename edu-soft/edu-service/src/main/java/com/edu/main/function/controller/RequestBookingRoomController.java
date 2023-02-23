package com.edu.main.function.controller;

import com.edu.main.function.dto.RequestBookingRoomDTO;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.RequestBookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/request-booking-room")
public class RequestBookingRoomController {

    @Autowired
    private RequestBookingRoomService requestService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<ResponseData> createRequest(@Validated @RequestBody RequestBookingRoomDTO requestDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Create request booking room successful")
                    .data(dtoMapper.toRequestBookingRoomDTO(requestService.createRequest(requestDTO))).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateRequest(@PathVariable final Long id, @RequestBody RequestBookingRoomDTO requestDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Update request booking room successful")
                    .data(dtoMapper.toRequestBookingRoomDTO(requestService.updateRequest(id, requestDTO))).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ResponseData> changeStatusForAdmin(@PathVariable final Long id,
                                                             @PathVariable final String status) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Change status of request booking room successful")
                    .data(dtoMapper.toRequestBookingRoomDTO(requestService.changeStatusRequestForAdmin(id, status))).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<ResponseData> getRequestsOfCurrentUser(@RequestParam(required = false) final String status,
                                                                 @RequestParam(required = false) final Integer page,
                                                                 @RequestParam(required = false) final Integer size) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get requests of current user successful")
                    .data(requestService.getRequestByStatusOfCurrentUser(status, page, size)).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseData> getRequests(@RequestParam(required = false) String username,
                                                    @RequestParam(required = false) final String status,
                                                    @RequestParam(required = false) final Integer page,
                                                    @RequestParam(required = false) final Integer size) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get requests successful")
                    .data(requestService.getRequestByStatus(username, status, page, size)).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
