package org.nuist.dto;

import lombok.Data;

@Data
public class AddTeacherDTO {

    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;

}
