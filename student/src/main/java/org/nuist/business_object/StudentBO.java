package org.nuist.business_object;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.nuist.persist_object.StudentPO;
import java.time.LocalDateTime;

/**
 * 学生业务对象，用于业务逻辑处理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentBO {
    
    private Long studentId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String grade;
    private String className;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 将业务对象转换为持久化对象
     * @return 学生持久化对象
     */
    public StudentPO toPO() {
        StudentPO po = new StudentPO();
        po.setStudentId(this.studentId);
        po.setUsername(this.username);
        po.setPassword(this.password);
        po.setEmail(this.email);
        po.setFullName(this.fullName);
        po.setPhone(this.phone);
        po.setGrade(this.grade);
        po.setClassName(this.className);
        po.setCreatedAt(this.createdAt);
        po.setUpdatedAt(this.updatedAt);
        return po;
    }
    
    /**
     * 将持久化对象转换为业务对象
     * @param po 学生持久化对象
     * @return 学生业务对象
     */
    public static StudentBO fromPO(StudentPO po) {
        return StudentBO.builder()
                .studentId(po.getStudentId())
                .username(po.getUsername())
                .password(po.getPassword())
                .email(po.getEmail())
                .fullName(po.getFullName())
                .phone(po.getPhone())
                .grade(po.getGrade())
                .className(po.getClassName())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
} 