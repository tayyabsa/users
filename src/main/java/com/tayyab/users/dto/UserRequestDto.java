package com.tayyab.users.dto;

import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NonNull
    private String password;
    private String fullName;
    @NonNull
    private String userName;
    private String email;
    private String phoneNo;
    private LocalDateTime dob;

}
