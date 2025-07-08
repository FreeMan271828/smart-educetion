package org.nuist.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UpdateTeacherDTO {

    private Long teacherId;

    private String email;
    private String fullName;
    private String phone;

}
