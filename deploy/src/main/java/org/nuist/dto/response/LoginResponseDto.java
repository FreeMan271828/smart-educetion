package org.nuist.dto.response;

import lombok.Builder;
import lombok.Data;
import org.nuist.entity.TokenResponse;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private TokenResponse tokens;
    private List<String> roles;
}
