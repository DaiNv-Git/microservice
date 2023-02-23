package com.edu.main.function.service;

import com.edu.main.function.dto.RequestPaperDTO;
import com.edu.main.function.entity.RequestPaper;
import javassist.NotFoundException;

import java.util.List;

public interface RequestPaperService {

    RequestPaper createRequestPaper(RequestPaperDTO requestPaperDTO) throws NotFoundException;

    RequestPaper updateRequestPaper(final Long id, RequestPaperDTO requestPaperDTO) throws NotFoundException;

    List<RequestPaper> getRequestPaperOfCurrentUser();

    RequestPaper changeStatusRequestPaper(final Long id, String status) throws NotFoundException;
}
