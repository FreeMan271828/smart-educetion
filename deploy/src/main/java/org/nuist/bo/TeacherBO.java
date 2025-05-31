package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.TeacherPO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherBO {
    private Long teacherId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TeacherBO fromTeacherPO(TeacherPO teacherPO) {
        return TeacherBO.builder()
                .teacherId(teacherPO.getTeacherId())
                .username(teacherPO.getUsername())
                .password(teacherPO.getPassword())
                .email(teacherPO.getEmail())
                .fullName(teacherPO.getFullName())
                .phone(teacherPO.getPhone())
                .createdAt(teacherPO.getCreatedAt())
                .updatedAt(teacherPO.getUpdatedAt())
                .build();
    }

    public TeacherPO toTeacherPO() {
        TeacherPO teacherPO = new TeacherPO();
        teacherPO.setTeacherId(teacherId);
        teacherPO.setUsername(username);
        teacherPO.setPassword(password);
        teacherPO.setEmail(email);
        teacherPO.setFullName(fullName);
        teacherPO.setPhone(phone);
        teacherPO.setCreatedAt(createdAt);
        teacherPO.setUpdatedAt(updatedAt);
        return teacherPO;
    }
}