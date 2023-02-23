package com.edu.main.function.controller;
import com.edu.main.function.dto.GradeDTO;
import com.edu.main.function.dto.ResponseData;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.GradeService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import static com.edu.main.function.dto.enums.ResponseDataStatus.ERROR;
import static com.edu.main.function.dto.enums.ResponseDataStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/grades")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping("/init")
    public ResponseEntity<ResponseData> initGrades(@RequestBody List<GradeDTO> gradeDTOS) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial grades successful")
                    .data(dtoMapper.toGradeDTO(gradeService.save(gradeDTOS))).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseData> getGradesOfUser(@RequestParam(required = false) final String username,
                                                        @RequestParam String sem) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get grades of user successful")
                    .data(dtoMapper.toGradeDTO(gradeService.getGradesOfUser(username, sem))).build(), HttpStatus.OK);
        } catch (Exception e) {
                return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Get grades detail successful")
                    .data(dtoMapper.toGradeDTO(gradeService.detail(id))).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseData> deleteGrades(@RequestParam List<Long> ids) {
        try {
             gradeService.deleteAllByIds(ids);
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("delete grades successful")
                    .data(null).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                    .message(e.getMessage())
                    .data(null).build(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("grade/import")
    public  ResponseEntity<ResponseData> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")) {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.toString())
                    .message("Initial grades successful")
                    .data(dtoMapper.toGradeDTO( gradeService.importExcel(file))).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseData.builder().status(ERROR.toString())
                .message("import grades fail")
                .data(null).build(), HttpStatus.BAD_REQUEST);
    }
}
