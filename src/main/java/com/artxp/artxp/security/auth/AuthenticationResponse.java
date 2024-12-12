package com.artxp.artxp.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
//Con esta respondemos la request
public class AuthenticationResponse {
    private String token;
}
