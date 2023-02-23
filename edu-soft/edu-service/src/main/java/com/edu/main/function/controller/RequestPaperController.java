package com.edu.main.function.controller;

import com.edu.main.function.dto.RequestPaperDTO;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.RequestPaperService;
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
import org.springframework.web.bind.annotation.RestController;

import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/papers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RequestPaperController {

    @Autowired
    private RequestPaperService requestPaperService;

    @Autowired
    private DTOMapper dtoMapper;

    @GetMapping
    public ResponseEntity<ResponseData> getRequestPaperOfUser() {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get request paper of current user successful")
                    .data(dtoMapper.toRequestPaperDTO(requestPaperService.getRequestPaperOfCurrentUser())).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseData> createRequestPaper(@Validated @RequestBody RequestPaperDTO requestDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Create request paper successful")
                    .data(dtoMapper.toRequestPaperDTO(requestPaperService.createRequestPaper(requestDTO))).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateRequestPaper(@PathVariable final Long id,
                                                           @Validated @RequestBody RequestPaperDTO requestDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Update request paper successful")
                    .data(dtoMapper.toRequestPaperDTO(requestPaperService.updateRequestPaper(id, requestDTO))).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ResponseData> changeStatusRequestPaper(@PathVariable final Long id,
                                                                 @PathVariable final String status) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Change status of request paper successful")
                    .data(dtoMapper.toRequestPaperDTO(requestPaperService.changeStatusRequestPaper(id, status))).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
}
