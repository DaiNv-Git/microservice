package com.edusoft.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edusoft.auth.dto.UserAppDTO;
import com.edusoft.auth.entity.UserApp;
import com.edusoft.auth.mapper.DTOMapper;
import com.edusoft.auth.repository.UserRepository;
import com.edusoft.auth.service.UserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Environment environment;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp userApp = userRepository.findByUsername(username);
        if (userApp == null) {
            log.error("User not found with username {}", username);
            throw new UsernameNotFoundException("Username not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userApp.getRoleApps().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(userApp.getUsername(), userApp.getPassword(),
                authorities);
    }

    @Override
    public Map<String, Object> signIn(final String username, final String password) {
        UserApp userApp = userRepository.findByUsername(username);
        if (userApp == null) {
            throw new IllegalArgumentException("User does not exist in the system!");
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            User user = (User) authentication.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(environment.getProperty("jwt.secret").getBytes());
            String access_token = JWT.create()
                    .withSubject(username)
                    .withIssuer(request.getRequestURL().toString())
                    .withExpiresAt(new Date(System.currentTimeMillis() +
                            Integer.parseInt(environment.getProperty("jwt.access.token.expire"))))
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .sign(algorithm);
//            String refresh_token = JWT.create()
//                    .withSubject(username)
//                    .withIssuer(request.getRequestURL().toString())
//                    .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(environment.getProperty
//                    ("jwt.refresh.token.expire"))))
//                    .sign(algorithm);
            log.info("User {} login success", username);
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("access_token", access_token);
//            mapData.put("refresh_token", refresh_token);
            mapData.put("roles",
                    user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            return mapData;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Username or password incorrect!");
        }
    }

    @Override
    public void importUser(List<UserAppDTO> userAppDTOS) {
        if (!CollectionUtils.isEmpty(userAppDTOS)) {
            List<UserApp> existedUser = userRepository.findAll();
            List<UserApp> users = dtoMapper.toUserApp(userAppDTOS);
            for (UserApp userApp : users) {
                Optional<UserApp> userExistedOptional = existedUser.stream().filter(u -> userApp.getUsername().equalsIgnoreCase(u.getUsername())).findFirst();
                if (userExistedOptional.isPresent()) {
                    userApp.setId(userExistedOptional.get().getId());
                }
            }
            if (!CollectionUtils.isEmpty(users)) {
                users.forEach(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));
                userRepository.saveAll(users);
            }
        }
    }

    @Override
    public UserApp getCurrentUser() throws NotFoundException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Can't get info of user current!");
            throw new IllegalArgumentException("Can't get info of user current!");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserApp userApp = userRepository.findByUsername(username);
        if (userApp == null) {
            log.error("User with username {} not found", username);
            throw new NotFoundException("Current user not found");
        }
        return userApp;
    }

    @Override
    public UserApp getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username can not be empty");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserApp> getUserByUsernames(List<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws IllegalArgumentException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Can't get info of user current!");
            throw new IllegalArgumentException("Can't get info of user current!");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (org.apache.commons.lang3.StringUtils.isEmpty(username)) {
            log.error("Username of user current is empty");
            throw new IllegalArgumentException("Can't get user of user current!");
        }
        UserApp userApp = this.getUserByUsername(username);
        if (!passwordEncoder.matches(oldPassword, userApp.getPassword())) {
            log.error("Old password is incorrect");
            throw new IllegalArgumentException("Old password is incorrect");
        }
        userApp.setPassword(passwordEncoder.encode(newPassword));
        log.info("Change password for user {} success", userApp.getUsername());
        userRepository.save(userApp);
    }
}
