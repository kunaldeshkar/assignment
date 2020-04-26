package com.amdocs.media.assignement.authservice.config;

import com.amdocs.media.assignement.authservice.payload.LoginPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class CustomizeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeAuthenticationFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("Cannot parse username or password", e.getCause());
        }

        String parsedReq = sb.toString();
        if (parsedReq != null) {
            ObjectMapper mapper = new ObjectMapper();
            LoginPayload loginRequest = null;
            try {
                loginRequest = mapper.readValue(parsedReq, LoginPayload.class);
            } catch (JsonProcessingException e) {
                LOGGER.error("Cannot parse username or password", e.getCause());
            }

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }

        LOGGER.error("Cannot read the json request body");
        return null;

    }


}
