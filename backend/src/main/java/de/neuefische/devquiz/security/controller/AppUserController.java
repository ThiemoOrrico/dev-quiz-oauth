package de.neuefische.devquiz.security.controller;

import de.neuefische.devquiz.security.model.AppUser;
import de.neuefische.devquiz.security.service.JWTUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class AppUserController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtilService jwtUtilService;


    @Autowired
    public AppUserController(AuthenticationManager authenticationManager, JWTUtilService jwtUtilService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("auth/login")
    public String login(@RequestBody AppUser appUser){

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword())
        );

        return jwtUtilService.createToken(new HashMap<>(), appUser.getUsername());
    }

}

