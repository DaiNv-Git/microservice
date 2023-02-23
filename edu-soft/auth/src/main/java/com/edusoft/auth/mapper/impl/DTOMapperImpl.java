package com.edusoft.auth.mapper.impl;

import com.edusoft.auth.dto.UserAppDTO;
import com.edusoft.auth.entity.RoleApp;
import com.edusoft.auth.entity.UserApp;
import com.edusoft.auth.mapper.DTOMapper;
import com.edusoft.auth.repository.RoleRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOMapperImpl implements DTOMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserApp toUserApp(UserAppDTO userAppDTO) {
        UserApp user = new UserApp();
        if (userAppDTO != null) {
            if (!StringUtils.isBlank(userAppDTO.getUsername())) {
                user.setUsername(userAppDTO.getUsername());
            }
            if (!StringUtils.isBlank(userAppDTO.getPassword())) {
                user.setPassword(userAppDTO.getPassword());
            }
            if (!StringUtils.isBlank(userAppDTO.getEmail())) {
                user.setEmail(userAppDTO.getEmail());
            }
            if (!CollectionUtils.isEmpty(userAppDTO.getRoles())) {
                List<RoleApp> roleApps = userAppDTO.getRoles().stream().map(r -> RoleApp.builder().name(r).build())
                        .collect(Collectors.toList());
//                userAppDTO.getRoles().forEach(r -> roleApps.add(RoleApp.builder().name(r).build()));
                if (!CollectionUtils.isEmpty(roleApps)) {
                    user.setRoleApps(roleApps);
                }
            }
        }
        return user;
    }

    @Override
    public List<UserApp> toUserApp(List<UserAppDTO> userAppDTOs) {
        List<UserApp> users = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userAppDTOs)) {
            userAppDTOs.forEach(u -> users.add(this.toUserApp(u)));
        }
        return users;
    }

    @Override
    public UserAppDTO toUserAppDTO(UserApp userApp) {
        UserAppDTO userAppDTO = new UserAppDTO();
        if (userApp != null) {
            if (!StringUtils.isBlank(userApp.getUsername())) {
                userAppDTO.setUsername(userApp.getUsername());
            }
            if (!StringUtils.isBlank(userApp.getEmail())) {
                userAppDTO.setEmail(userApp.getEmail());
            }
            if (!CollectionUtils.isEmpty(userApp.getRoleApps())) {
                List<String> roleNames = new ArrayList<>();
                userApp.getRoleApps().forEach(r -> roleNames.add(r.getName()));
                if (!CollectionUtils.isEmpty(roleNames)) {
                    userAppDTO.setRoles(roleNames);
                }
            }
        }
        return userAppDTO;
    }

    @Override
    public List<UserAppDTO> toUserAppDTO(List<UserApp> userApps) {
        List<UserAppDTO> userAppDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userApps)) {
            userApps.stream().forEach(u -> userAppDTOs.add(this.toUserAppDTO(u)));
        }
        return userAppDTOs;
    }
}
