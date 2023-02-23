package com.edu.main.function.service.impl;

import com.edu.main.function.clients.AuthClients;
import com.edu.main.function.dto.RequestPaperDTO;
import com.edu.main.function.dto.UserAppDTO;
import com.edu.main.function.dto.enums.PaperType;
import com.edu.main.function.dto.enums.RequestStatus;
import com.edu.main.function.entity.RequestPaper;
import com.edu.main.function.mapper.DTOMapper;
import com.edu.main.function.repository.RequestPaperRepository;
import com.edu.main.function.service.RequestPaperService;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestPaperServiceImpl implements RequestPaperService {

    @Autowired
    private RequestPaperRepository requestPaperRepository;

    @Autowired
    private AuthClients authClients;

    @Autowired
    private DTOMapper dtoMapper;

    @Override
    public RequestPaper createRequestPaper(RequestPaperDTO requestPaperDTO) throws NotFoundException {
        UserAppDTO user = authClients.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Can not get current user");
        }
        RequestPaper requestPaper = dtoMapper.toRequestPaper(requestPaperDTO);
        requestPaper.setUsername(user.getUsername());
        requestPaper.setStatus(RequestStatus.PENDING);
        return requestPaperRepository.save(requestPaper);
    }

    @Override
    public RequestPaper updateRequestPaper(final Long id, RequestPaperDTO requestPaperDTO) throws NotFoundException {
        RequestPaper requestPaper = this.getById(id);
        if (!StringUtils.isBlank(requestPaperDTO.getStatus())) {
            RequestStatus requestStatus = dtoMapper.convertToRequestStatus(requestPaperDTO.getStatus());
            requestPaper.setStatus(requestStatus);
        }
        if (!StringUtils.isBlank(requestPaperDTO.getType())) {
            PaperType paperType = dtoMapper.convertToPaperType(requestPaperDTO.getType());
            requestPaper.setType(paperType);
        }
        return requestPaperRepository.save(requestPaper);
    }

    @Override
    public List<RequestPaper> getRequestPaperOfCurrentUser() {
        UserAppDTO currentUser = authClients.getCurrentUser();
        if (!CollectionUtils.isEmpty(currentUser.getRoles())
                && currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r))) {
            return requestPaperRepository.findAll().stream()
                    .sorted(Comparator.comparingLong(RequestPaper::getId).reversed()).collect(Collectors.toList());
        }
        return requestPaperRepository.findByUsername(currentUser.getUsername()).stream()
                .sorted(Comparator.comparingLong(RequestPaper::getId).reversed()).collect(Collectors.toList());
    }

    @Override
    public RequestPaper changeStatusRequestPaper(Long id, String status) throws NotFoundException {
        RequestPaper requestPaper = this.getById(id);
        requestPaper.setStatus(dtoMapper.convertToRequestStatus(status));
        return requestPaperRepository.save(requestPaper);
    }

    private RequestPaper getById(final Long id) throws NotFoundException {
        return requestPaperRepository.findById(id).orElseThrow(() -> new NotFoundException("Request paper not found"));
    }
}
