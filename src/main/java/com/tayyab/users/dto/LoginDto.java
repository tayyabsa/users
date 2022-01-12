package com.tayyab.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NonNull
    private String userName;

    @NonNull
    private String password;
}
