package com.edu.main.function.service;

import com.edu.main.function.dto.SubjectMemberDTO;
import com.edu.main.function.entity.SubjectMember;
import javassist.NotFoundException;

import java.util.List;

public interface SubjectMemberService {

    SubjectMember save(SubjectMemberDTO subjectMemberDTO) throws NotFoundException;

    List<SubjectMember> save(List<SubjectMemberDTO> subjectMemberDTOs) throws NotFoundException;

    List<SubjectMember> getMemberOfSubject(Long subjectId) throws NotFoundException;

    List<SubjectMember> getByUsername(final String username);
}
