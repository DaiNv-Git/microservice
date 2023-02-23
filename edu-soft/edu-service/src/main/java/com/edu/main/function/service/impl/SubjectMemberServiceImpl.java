package com.edu.main.function.service.impl;

import com.edu.main.function.dto.SubjectMemberDTO;
import com.edu.main.function.entity.Subject;
import com.edu.main.function.entity.SubjectMember;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.SubjectMemberRepository;
import com.edu.main.function.service.SubjectMemberService;
import com.edu.main.function.service.SubjectService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectMemberServiceImpl implements SubjectMemberService {

    @Autowired
    private SubjectMemberRepository memberRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private SubjectService subjectService;

    @Override
    public SubjectMember save(SubjectMemberDTO subjectMemberDTO) throws NotFoundException {
        if (subjectMemberDTO == null) {
            throw new IllegalArgumentException("Data invalid");
        }
        if (subjectMemberDTO.getSubjectId() == null) {
            throw new IllegalArgumentException("Subject id can not be null");
        }
        Subject subject = subjectService.getById(subjectMemberDTO.getSubjectId());
        SubjectMember subjectMember = dtoMapper.toSubjectMember(subjectMemberDTO);
        subjectMember.setSubject(subject);
        return memberRepository.save(subjectMember);
    }

    @Override
    public List<SubjectMember> save(List<SubjectMemberDTO> subjectMemberDTOs) throws NotFoundException {
        List<SubjectMember> subjectMembers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subjectMemberDTOs)) {
            List<Subject> subjects = subjectService.getAll();
            for (SubjectMemberDTO memberDTO : subjectMemberDTOs) {
                if (memberDTO == null) {
                    throw new IllegalArgumentException("Data invalid");
                }
                if (memberDTO.getSubjectId() == null) {
                    throw new IllegalArgumentException("Subject id can not be null");
                }
                Optional<Subject> optionalSubject =
                        subjects.stream().filter(s -> memberDTO.getSubjectId() == s.getId()).findFirst();
                if (!optionalSubject.isPresent()) {
                    throw new IllegalArgumentException("Subject with id " + memberDTO.getSubjectId() + " not found");
                }
                SubjectMember member = dtoMapper.toSubjectMember(memberDTO);
                member.setSubject(optionalSubject.get());
                subjectMembers.add(member);
            }
        }
        return memberRepository.saveAll(subjectMembers);
    }

    @Override
    public List<SubjectMember> getMemberOfSubject(Long subjectId) throws NotFoundException {
        Subject subject = subjectService.getById(subjectId);
        return memberRepository.findBySubject(subject);
    }

    @Override
    public List<SubjectMember> getByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
