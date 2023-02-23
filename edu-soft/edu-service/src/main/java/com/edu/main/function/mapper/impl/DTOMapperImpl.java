package com.edu.main.function.mapper.impl;

import com.edu.main.function.dto.ExamDTO;
import com.edu.main.function.dto.GradeDTO;
import com.edu.main.function.dto.NotificationDTO;
import com.edu.main.function.dto.RequestBookingRoomDTO;
import com.edu.main.function.dto.RequestPaperDTO;
import com.edu.main.function.dto.RoomDTO;
import com.edu.main.function.dto.SubjectDTO;
import com.edu.main.function.dto.SubjectMemberDTO;
import com.edu.main.function.dto.TimeTableDTO;
import com.edu.main.function.dto.enums.PaperType;
import com.edu.main.function.dto.enums.ReferenceNotify;
import com.edu.main.function.dto.enums.RequestStatus;
import com.edu.main.function.dto.enums.Semester;
import com.edu.main.function.entity.Exam;
import com.edu.main.function.entity.Grade;
import com.edu.main.function.entity.Notification;
import com.edu.main.function.entity.RequestBookingRoom;
import com.edu.main.function.entity.RequestPaper;
import com.edu.main.function.entity.Room;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.service.SubjectService;
import com.edu.main.function.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
public class DTOMapperImpl implements DTOMapper {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private SubjectService subjectService;

    @Override
    public List<GradeDTO> toGradeDTO(List<Grade> grades) {
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(grades)) {
            grades.stream().forEach(g -> gradeDTOS.add(this.toGradeDTO(g)));
        }
        return gradeDTOS;
    }

    @Override
    public GradeDTO toGradeDTO(Grade grade) {
        GradeDTO gradeDTO = new GradeDTO();
        if (grade != null) {
            if (!StringUtils.isBlank(grade.getUsername())) {
                gradeDTO.setUsername(grade.getUsername());
            }
            if (grade.getId() != null) {
                gradeDTO.setId(grade.getId());
            }
            if (grade.getCgpa4() != null) {
                gradeDTO.setCgpa4(grade.getCgpa4());
            }
            if (grade.getCgpa100() != null) {
                gradeDTO.setCgpa100(grade.getCgpa100());
            }
            if (!StringUtils.isBlank(grade.getCreditsAccumulated())) {
                gradeDTO.setCreditsAccumulated(grade.getCreditsAccumulated());
            }
            if (!StringUtils.isBlank(grade.getClassification())) {
                gradeDTO.setClassification(grade.getClassification());
            }
            if (grade.getSemester() != null) {
                gradeDTO.setSemester(grade.getSemester().toString());
            }
        }
        return gradeDTO;
    }
    @Override
    public List<Grade> toGrade(List<GradeDTO> gradeDTOs) {
        List<Grade> grades = new ArrayList<>();
        if (!CollectionUtils.isEmpty(gradeDTOs)) {
            grades=  dtoToGrade(gradeDTOs);
        }
        return grades;
    }
    public List<Grade> dtoToGrade(List<GradeDTO> gradeDTOs) {
        List<Grade> grades = new ArrayList<>();
        String userName  ="";
        String creditsAccumulated="";
        Double cgpa100 =0.0;
        Double cgpa4 =0.0;
        String classification = "";
        Semester semester = null;
        Long id =null;
       for(int i = 0 ; i <gradeDTOs.size();i++){
           if (gradeDTOs.get(i).getId()!=null) {
               id = gradeDTOs.get(i).getId();
           }
           if (!StringUtils.isBlank(gradeDTOs.get(i).getUsername())) {
             userName = gradeDTOs.get(i).getUsername();
           }
           if (!StringUtils.isBlank(gradeDTOs.get(i).getCreditsAccumulated())) {
                creditsAccumulated = gradeDTOs.get(i).getCreditsAccumulated();
           }

           if (gradeDTOs.get(i).getCgpa100()!=null) {
                cgpa100 = gradeDTOs.get(i).getCgpa100();
           }
           if (gradeDTOs.get(i).getCgpa4()!=null) {
               cgpa4 = gradeDTOs.get(i).getCgpa4();
           }
           if (!StringUtils.isBlank(gradeDTOs.get(i).getClassification())) {
                classification=gradeDTOs.get(i).getClassification();
           }
           if (!StringUtils.isBlank(gradeDTOs.get(i).getSemester())) {
                semester = this.convertToSemester(gradeDTOs.get(i).getSemester());
           }
           grades.add(new Grade(id,cgpa100,creditsAccumulated,classification,userName,semester,cgpa4));
       }
       return grades;
    }

    @Override
    public Grade toGrade(GradeDTO gradeDTO) {
        Grade grade = new Grade();
        if (gradeDTO != null) {
            if (!StringUtils.isBlank(gradeDTO.getUsername())) {
                grade.setUsername(gradeDTO.getUsername());
            }
            if (!StringUtils.isBlank(gradeDTO.getCreditsAccumulated())) {
                grade.setCreditsAccumulated(gradeDTO.getCreditsAccumulated());
            }
            if (!StringUtils.isBlank(gradeDTO.getClassification())) {
                grade.setClassification(gradeDTO.getClassification());
            }
            if (!StringUtils.isBlank(gradeDTO.getSemester())) {
                Semester semester = this.convertToSemester(gradeDTO.getSemester());
                grade.setSemester(semester);
            }
        }
        return grade;
    }

    @Override
    public List<RoomDTO> toRoomDTO(List<Room> rooms) {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rooms)) {
            rooms.stream().forEach(r -> roomDTOS.add(this.toRoomDTO(r)));
        }
        return roomDTOS;
    }

    @Override
    public RoomDTO toRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        if (room != null) {
            if (!StringUtils.isBlank(room.getName())) {
                roomDTO.setName(room.getName());
            }
            if (room.getStatus() != null) {
                roomDTO.setStatus(room.getStatus());
            }
        }
        return roomDTO;
    }

    @Override
    public Room toRoom(RoomDTO roomDTO) {
        Room room = new Room();
        if (roomDTO != null) {
            if (!StringUtils.isBlank(roomDTO.getName())) {
                room.setName(roomDTO.getName());
            }
        }
        return room;
    }

    @Override
    public List<Room> toRoom(List<RoomDTO> roomDTOs) {
        List<Room> rooms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roomDTOs)) {
            roomDTOs.forEach(r -> rooms.add(this.toRoom(r)));
        }
        return rooms;
    }

    @Override
    public RequestBookingRoom toRequestBookingRoom(RequestBookingRoomDTO requestDTO) {
        RequestBookingRoom request = new RequestBookingRoom();
        if (requestDTO != null) {
            if (!StringUtils.isBlank(requestDTO.getDescription())) {
                request.setDescription(requestDTO.getDescription());
            }
            if (!StringUtils.isBlank(requestDTO.getUsername())) {
                request.setUsername(requestDTO.getUsername());
            }
            if (requestDTO.getStartTime() != null) {
                request.setStartTime(requestDTO.getStartTime());
            }
            if (requestDTO.getEndTime() != null) {
                request.setEndTime(requestDTO.getEndTime());
            }
        }
        return request;
    }

    @Override
    public RequestBookingRoomDTO toRequestBookingRoomDTO(RequestBookingRoom request) {
        RequestBookingRoomDTO requestDTO = new RequestBookingRoomDTO();
        if (request != null) {
            if (!StringUtils.isBlank(request.getDescription())) {
                requestDTO.setDescription(request.getDescription());
            }
            if (request.getStartTime() != null) {
                requestDTO.setStartTime(request.getStartTime());
            }
            if (request.getEndTime() != null) {
                requestDTO.setEndTime(request.getEndTime());
            }
            if (!StringUtils.isBlank(request.getUsername())) {
                requestDTO.setUsername(request.getUsername());
            }
            if (request.getRoom() != null) {
                requestDTO.setRoomName(request.getRoom().getName());
            }
            if (request.getStatus() != null) {
                requestDTO.setStatus(request.getStatus().getName());
            }
        }
        return requestDTO;
    }

    @Override
    public RequestPaper toRequestPaper(RequestPaperDTO requestPaperDTO) {
        RequestPaper requestPaper = new RequestPaper();
        if (requestPaperDTO != null) {
            if (!StringUtils.isBlank(requestPaperDTO.getUsername())) {
                requestPaper.setUsername(requestPaperDTO.getUsername());
            }
            if (!StringUtils.isBlank(requestPaperDTO.getType())) {
                PaperType paperType = this.convertToPaperType(requestPaperDTO.getType());
                requestPaper.setType(paperType);
            }
        }
        return requestPaper;
    }

    @Override
    public PaperType convertToPaperType(String type) {
        try {
            return PaperType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Paper type invalid");
        }
    }

    @Override
    public RequestStatus convertToRequestStatus(final String status) {
        try {
            return RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Request status invalid");
        }
    }

    @Override
    public RequestPaperDTO toRequestPaperDTO(RequestPaper requestPaper) {
        RequestPaperDTO requestPaperDTO = new RequestPaperDTO();
        if (requestPaper != null) {
            if (requestPaper.getId() != null) {
                requestPaperDTO.setId(requestPaper.getId());
            }
            if (!StringUtils.isBlank(requestPaper.getUsername())) {
                requestPaperDTO.setUsername(requestPaper.getUsername());
            }
            if (requestPaper.getType() != null) {
                requestPaperDTO.setType(requestPaper.getType().getName());
            }
            if (requestPaper.getStatus() != null) {
                requestPaperDTO.setStatus(requestPaper.getStatus().getName());
            }
        }
        return requestPaperDTO;
    }

    @Override
    public List<RequestPaperDTO> toRequestPaperDTO(List<RequestPaper> requestPapers) {
        List<RequestPaperDTO> requestPaperDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(requestPapers)) {
            requestPapers.stream().forEach(r -> requestPaperDTOS.add(this.toRequestPaperDTO(r)));
        }
        return requestPaperDTOS;
    }

    @Override
    public Subject toSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        if (subjectDTO != null) {
            if (!StringUtils.isBlank(subjectDTO.getCode())) {
                subject.setCode(subjectDTO.getCode());
            }
            if (!StringUtils.isBlank(subjectDTO.getName())) {
                subject.setName(subjectDTO.getName());
            }
            if (subjectDTO.getStartDate() != null) {
                subject.setStartDate(subjectDTO.getStartDate());
            }
            if (subjectDTO.getEndDate() != null) {
                subject.setEndDate(subjectDTO.getEndDate());
            }
            if (!StringUtils.isBlank(subjectDTO.getRoomName())) {
                subject.setRoomName(subjectDTO.getRoomName());
            }
            if (!StringUtils.isBlank(subjectDTO.getStartTime())) {
                subject.setStartTime(subjectDTO.getStartTime());
            }
            if (!StringUtils.isBlank(subjectDTO.getEndTime())) {
                subject.setEndTime(subjectDTO.getEndTime());
            }
        }
        return subject;
    }

    @Override
    public List<Subject> toSubject(List<SubjectDTO> subjectDTOs) {
        List<Subject> subjects = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subjectDTOs)) {
            subjectDTOs.stream().forEach(s -> subjects.add(this.toSubject(s)));
        }
        return subjects;
    }

    @Override
    public SubjectMember toSubjectMember(SubjectMemberDTO memberDTO) {
        SubjectMember subjectMember = new SubjectMember();
        if (memberDTO != null) {
            if (!StringUtils.isBlank(memberDTO.getUsername())) {
                subjectMember.setUsername(memberDTO.getUsername());
            }
        }
        return subjectMember;
    }

    @Override
    public SubjectDTO toSubjectDTO(Subject subject) {
        SubjectDTO subjectDTO = new SubjectDTO();
        if (subject != null) {
            if (subject.getId() != null) {
                subjectDTO.setId(subject.getId());
            }
            if (!StringUtils.isBlank(subject.getCode())) {
                subjectDTO.setCode(subject.getCode());
            }
            if (!StringUtils.isBlank(subject.getName())) {
                subjectDTO.setName(subject.getName());
            }
            if (subject.getStartDate() != null) {
                subjectDTO.setStartDate(subject.getStartDate());
            }
            if (subject.getEndDate() != null) {
                subjectDTO.setEndDate(subject.getEndDate());
            }
            if (!StringUtils.isBlank(subject.getRoomName())) {
                subjectDTO.setRoomName(subject.getRoomName());
            }
            if (!StringUtils.isBlank(subject.getStartTime())) {
                subjectDTO.setStartTime(subject.getStartTime());
            }
            if (!StringUtils.isBlank(subject.getEndTime())) {
                subjectDTO.setEndTime(subject.getEndTime());
            }
        }
        return subjectDTO;
    }

    @Override
    public List<SubjectDTO> toSubjectDTO(List<Subject> subjects) {
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subjects)) {
            subjects.stream().forEach(s -> subjectDTOS.add(this.toSubjectDTO(s)));
        }
        return subjectDTOS;
    }

    @Override
    public NotificationDTO toNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        if (notification != null) {
            if (notification.getId() != null) {
                notificationDTO.setId(notification.getId());
            }
            if (notification.getEventDate() != null) {
                notificationDTO.setEventDate(notification.getEventDate());
            }
            if (notification.getIsRead() != null) {
                notificationDTO.setIsRead(notification.getIsRead());
            }
            if (notification.getIsImportant() != null) {
                notificationDTO.setIsImportant(notification.getIsImportant());
            }
            if (!StringUtils.isBlank(notification.getTitle())) {
                notificationDTO.setTitle(notification.getTitle());
            }
            if (!StringUtils.isBlank(notification.getMessage())) {
                notificationDTO.setMessage(notification.getMessage());
            }
            if (notification.getCreatedDate() != null) {
                notificationDTO.setCreatedDate(notification.getCreatedDate());
            }
            if (notification.getModifiedDate() != null) {
                notificationDTO.setModifiedDate(notification.getModifiedDate());
            }
            if (notification.getReference() != null) {
                ReferenceNotify reference = notification.getReference();
                if (!ReferenceNotify.ALL.toString().equalsIgnoreCase(reference.toString())) {
                    Subject subject = subjectService.getByCode(reference.toString());
                    if (subject != null) {
                        notificationDTO.setSubjectId(subject.getId());
                        notificationDTO.setSubjectName(subject.getName());
                    }
                }
            }
        }
        return notificationDTO;
    }

    @Override
    public List<NotificationDTO> toNotificationDTO(List<Notification> notifications) {
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notifications)) {
            notifications.stream().forEach(n -> notificationDTOs.add(this.toNotificationDTO(n)));
        }
        return notificationDTOs;
    }

    @Override
    public Semester convertToSemester(String semester) {
        try {
            return Semester.valueOf(semester);
        } catch (Exception e) {
            throw new IllegalArgumentException("Semester invalid");
        }
    }

    @Override
    public Notification toNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        if (notificationDTO != null) {
            if (!StringUtils.isBlank(notificationDTO.getTitle())) {
                notification.setTitle(notificationDTO.getTitle());
            }
            if (!StringUtils.isBlank(notificationDTO.getMessage())) {
                notification.setMessage(notificationDTO.getMessage());
            }
            if (notificationDTO.getEventDate() != null) {
                notification.setEventDate(notificationDTO.getEventDate());
            }
            if (notificationDTO.getIsUrgent() != null) {
                notification.setIsUrgent(notificationDTO.getIsUrgent());
            }
        }
        return notification;
    }

    @Override
    public ReferenceNotify convertToReferenceNotify(String code) {
        try {
            return ReferenceNotify.valueOf(code);
        } catch (Exception e) {
            throw new IllegalArgumentException("Reference notify invalid");
        }
    }

    @Override
    public ExamDTO toExamDTO(Exam exam) {
        ExamDTO examDTO = new ExamDTO();
        if (exam != null) {
            if (exam.getSubject() != null) {
                examDTO.setSubjectCode(exam.getSubject().getCode());
                examDTO.setSubjectName(exam.getSubject().getName());
            }
            if (exam.getTime() != null) {
                examDTO.setTime(exam.getTime());
            }
            if (!StringUtils.isBlank(exam.getRoomName())) {
                examDTO.setRoomName(exam.getRoomName());
            }
        }
        return examDTO;
    }

    @Override
    public List<ExamDTO> toExamDTO(List<Exam> exams) {
        List<ExamDTO> examDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(exams)) {
            exams.stream().forEach(e -> examDTOS.add(this.toExamDTO(e)));
        }
        return examDTOS;
    }

    @Override
    public TimeTableDTO toTimeTableDTO(Subject subject) {
        TimeTableDTO timeTableDTO = new TimeTableDTO();
        if (subject != null) {
            if (!StringUtils.isBlank(subject.getName())) {
                timeTableDTO.setSubjectName(subject.getName());
            }
            if (!StringUtils.isBlank(subject.getRoomName())) {
                timeTableDTO.setRoomName(subject.getRoomName());
            }
            if (!StringUtils.isBlank(subject.getStartTime())) {
                timeTableDTO.setStartTime(subject.getStartTime());
            }
            if (!StringUtils.isBlank(subject.getEndTime())) {
                timeTableDTO.setEndTime(subject.getEndTime());
            }
            if (subject.getStartDate() != null && !StringUtils.isBlank(subject.getStartTime())) {
                timeTableDTO.setStartDate(DateUtil.setTimeForDate(subject.getStartDate(), subject.getStartTime()));
            }
            if (subject.getEndDate() != null && !StringUtils.isBlank(subject.getEndTime())) {
                timeTableDTO.setEndDate(DateUtil.setTimeForDate(subject.getEndDate(), subject.getEndTime()));
            }
        }
        return timeTableDTO;
    }

    @Override
    public List<TimeTableDTO> toTimeTableDTO(List<Subject> subjects) {
        List<TimeTableDTO> timeTableDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subjects)) {
            subjects.stream().forEach(s -> timeTableDTOs.add(this.toTimeTableDTO(s)));
        }
        return timeTableDTOs;
    }
}
