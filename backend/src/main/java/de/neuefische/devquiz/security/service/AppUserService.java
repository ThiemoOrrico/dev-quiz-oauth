package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.security.model.AppUser;

import de.neuefische.devquiz.security.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepo appUserRepo;

    @Autowired
    public AppUserService(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public AppUser addNewUser(AppUser appUser){
        return appUserRepo.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        return appUserRepo.findById(userName)
                .map(appUser -> User
                        .withUsername(appUser.getUsername())
                        .password(appUser.getPassword())
                        .authorities("user")
                        .build())
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist: "+ userName));
    }

    public String getUserByUserName(String username) {
        Optional<AppUser> optionalAppUserData = appUserRepo.findById(username);
        if (optionalAppUserData.isPresent()){
            return "Hallo " + optionalAppUserData.get().getUsername();
        } else {
            throw new UsernameNotFoundException("No User found by username: " + username);
        }
    }
}

