package org.nuist.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotNull
    String refreshToken;
}
