package com.programmingtechie.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse  {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;
}
