package com.app.models.responses;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExceptionResponse {
    private int statusCode;
    private String exceptionType;
    private Object details;
    private LocalDateTime timestamp;
    private String path;
}
