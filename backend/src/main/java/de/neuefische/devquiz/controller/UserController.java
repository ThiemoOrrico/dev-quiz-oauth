package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.model.UserResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("user/me")
    public UserResponseModel getLoggedInUser(Principal principal){
        return new UserResponseModel(principal.getName());

    }

}
