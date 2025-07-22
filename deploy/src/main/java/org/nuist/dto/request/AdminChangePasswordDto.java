package org.nuist.dto.request;

import lombok.Data;

@Data
public class AdminChangePasswordDto {
    private String username;
    private String newPassword;
}
