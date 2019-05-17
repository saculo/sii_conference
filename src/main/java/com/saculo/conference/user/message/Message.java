package com.saculo.conference.user.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Message {

    private String receiver;
    private String content;
    private LocalDateTime sentAt;

    @Override
    public String toString() {
        return "Message{" +
                "To = '" + receiver + '\'' +
                ", content = '" + content + '\'' +
                ", sentAt = " + sentAt +
                '}';
    }
}
