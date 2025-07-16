package org.nuist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Message.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;
}
