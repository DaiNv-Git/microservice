package com.edusoft.auth.mapper;

import com.edusoft.auth.dto.UserAppDTO;
import com.edusoft.auth.entity.UserApp;

import java.util.List;

public interface DTOMapper {

    UserApp toUserApp(UserAppDTO userAppDTO);

    List<UserApp> toUserApp(List<UserAppDTO> userAppDTOs);

    UserAppDTO toUserAppDTO(UserApp userApp);

    List<UserAppDTO> toUserAppDTO(List<UserApp> userApps);
}
