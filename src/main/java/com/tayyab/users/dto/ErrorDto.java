package com.tayyab.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDto implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant timestamp;
    private String code;
    private String message;
    private String path;
    @JsonIgnore
    private String detailMessage;

    public ErrorDto(String code, String message, String path){
        this.timestamp = Instant.now();
        this.code = code;
        this.message = message;
        this.path = path;
    }


}
