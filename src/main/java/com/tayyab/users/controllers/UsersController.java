package com.tayyab.users.controllers;

import com.tayyab.users.dto.LoginDto;
import com.tayyab.users.dto.UserRequestDto;
import com.tayyab.users.dto.UserResponseDto;
import com.tayyab.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto userRequest) {
        return ResponseEntity.ok(usersService.createUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> me(@RequestBody UserRequestDto userRequest) {
        return ResponseEntity.ok().body(usersService.me(userRequest.getUserName()));
    }
}
