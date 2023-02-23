package com.edu.main.function.controller;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.dto.RoomDTO;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private AuthClients authClients;

    @GetMapping
    public ResponseEntity<ResponseData> getRooms(@RequestParam(required = false) final Boolean status,
                                                 @RequestParam(required = false) final Integer page,
                                                 @RequestParam(required = false) final Integer size) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get rooms successful")
                    .data(dtoMapper.toRoomDTO(roomService.getRoomByStatus(status, page, size)))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/init")
    public ResponseEntity<ResponseData> initRooms(@RequestBody List<RoomDTO> roomDTOs) {
        try {
            roomService.initRooms(roomDTOs);
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial rooms successful").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        System.out.println("This is edu service");
        return new ResponseEntity<>("This is edu service", HttpStatus.OK);
    }
}
