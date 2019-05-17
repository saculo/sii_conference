package com.saculo.conference.user.message;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class MessageManager {

    public void sendMessage(Message message) {
        try {
            FileWriter geek_file = new FileWriter("emails.txt");
            BufferedWriter writer = new BufferedWriter(geek_file);
            writer.append(message.toString());
            writer.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
