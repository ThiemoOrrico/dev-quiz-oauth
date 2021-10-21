package de.neuefische.devquiz.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserData {

    private String AppUserFirstName;
    private String AppUserName;
    private String AppUserEMail;

}
