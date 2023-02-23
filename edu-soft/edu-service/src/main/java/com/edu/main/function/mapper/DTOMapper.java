package com.edu.main.function.mapper;

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

import java.util.List;

public interface DTOMapper {

    Room toRoom(RoomDTO roomDTO);

    RoomDTO toRoomDTO(Room room);

    List<RoomDTO> toRoomDTO(List<Room> rooms);

    List<Room> toRoom(List<RoomDTO> roomDTOs);

    RequestBookingRoom toRequestBookingRoom(RequestBookingRoomDTO requestDTO);

    RequestBookingRoomDTO toRequestBookingRoomDTO(RequestBookingRoom request);

    Grade toGrade(GradeDTO gradeDTO);

    List<Grade> toGrade(List<GradeDTO> gradeDTOs);

    GradeDTO toGradeDTO(Grade grade);

    List<GradeDTO> toGradeDTO(List<Grade> grades);

    RequestStatus convertToRequestStatus(final String status);

    PaperType convertToPaperType(final String type);

    RequestPaper toRequestPaper(RequestPaperDTO requestPaperDTO);

    RequestPaperDTO toRequestPaperDTO(RequestPaper requestPaper);

    List<RequestPaperDTO> toRequestPaperDTO(List<RequestPaper> requestPapers);

    Subject toSubject(SubjectDTO subjectDTO);

    List<Subject> toSubject(List<SubjectDTO> subjectDTOs);

    SubjectMember toSubjectMember(SubjectMemberDTO memberDTO);

    SubjectDTO toSubjectDTO(Subject subject);

    List<SubjectDTO> toSubjectDTO(List<Subject> subjects);

    NotificationDTO toNotificationDTO(Notification notification);

    List<NotificationDTO> toNotificationDTO(List<Notification> notifications);

    Semester convertToSemester(String semester);

    Notification toNotification(NotificationDTO notificationDTO);

    ReferenceNotify convertToReferenceNotify(String code);

    ExamDTO toExamDTO(Exam exam);

    List<ExamDTO> toExamDTO(List<Exam> exams);

    TimeTableDTO toTimeTableDTO(Subject subject);

    List<TimeTableDTO> toTimeTableDTO(List<Subject> subjects);
}
