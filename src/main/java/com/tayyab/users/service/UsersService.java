package com.tayyab.users.service;

import com.tayyab.users.config.security.UsersPrinciple;
import com.tayyab.users.dto.Roles;
import com.tayyab.users.dto.UserRequestDto;
import com.tayyab.users.dto.UserResponseDto;
import com.tayyab.users.entity.RoleEntity;
import com.tayyab.users.entity.UsersEntity;
import com.tayyab.users.exception.ApplicationException;
import com.tayyab.users.repository.RolesRepository;
import com.tayyab.users.repository.UsersRepository;
import com.tayyab.users.repository.specifications.UsersSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public UserResponseDto me(String username) {
        Optional<UsersEntity> userName = usersRepository.findByUserName(username);
        return toUserResponse(userName.get());
    }

    public UserResponseDto createUser(UserRequestDto userRequest) {

        validateUsername(userRequest.getUserName());
        UsersEntity user = UsersEntity.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userRequest.getUserName())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .dob(userRequest.getDob())
                .phoneNo(userRequest.getPhoneNo())
                .fullName(userRequest.getFullName())
                .createdAt(LocalDateTime.now()).build();

        user.setRoles(getRoles(Roles.ROLE_USER.name()));
        UsersEntity savedUser = usersRepository.save(user);

        return toUserResponse(savedUser);
    }

    private void validateUsername(String userName) {
        if (usersRepository.findByUserName(userName).isPresent()) {
            throw new ApplicationException("4000", "User name already exits");
        }
    }

    private static final UserResponseDto toUserResponse(UsersEntity users) {
        return UserResponseDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .email(users.getEmail())
                .dob(users.getDob())
                .fullName(users.getFullName())
                .phoneNo(users.getPhoneNo())
                .createdAt(users.getCreatedAt()).build();
    }

    private Set<RoleEntity> getRoles(String role) {
        Set<RoleEntity> roleEntities = new HashSet<>();
        Optional<RoleEntity> roleEntity = rolesRepository.findByRole(role);
        if (roleEntity.isPresent()) {
            roleEntities.add(roleEntity.get());
        }
        return roleEntities;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UsersEntity> usersEntity = usersRepository.findByUserName(userName);
        if (!usersEntity.isPresent()) {
            throw new UsernameNotFoundException(userName);
        }
        return new UsersPrinciple(usersEntity.get());
    }

    public List<UserResponseDto> getLongTermUsers() {
        return usersRepository.findAll(UsersSpecifications.isLongTermUser())
                .stream().map(UsersService::toUserResponse).collect(Collectors.toList());
    }
}
