package org.nuist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// ChatRequest.java
@Data
public class ChatHistoryRequest {
    private List<Message> messages = new ArrayList<>();

    public boolean isValid() {
        return messages != null && !messages.isEmpty();
    }

    public void addCourseContext(String courseName) {
        if (!messages.isEmpty()) {
            // 只修改最后一条用户消息[3,4](@ref)
            Message lastMessage = messages.get(messages.size() - 1);
            if ("user".equals(lastMessage.getRole())) {
                lastMessage.setContent("关于课程《" + courseName + "》的问题：" + lastMessage.getContent());
            }
        }
    }
}