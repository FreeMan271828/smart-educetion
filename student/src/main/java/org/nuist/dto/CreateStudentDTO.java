package org.nuist.dto;

import lombok.Data;

@Data
public class CreateStudentDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
}
