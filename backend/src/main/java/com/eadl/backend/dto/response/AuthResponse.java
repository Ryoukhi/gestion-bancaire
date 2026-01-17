package com.eadl.backend.dto.response;

import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private String fullName;
}