package org.nuist.dto.request;

import lombok.Data;
import org.nuist.bo.AiMessage;

import java.util.List;

@Data
public class ChatRequestDto {
    List<AiMessage> messages;
}
