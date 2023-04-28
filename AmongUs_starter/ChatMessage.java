/*
 * @Instructor: Alan Mutka
 * @author: Rinor Bugujevci, Uejs Hamja
 * Project: Among us
 * @version: 28/04/2023
 * ISTE: 121.801
 */

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String sender;
    private String message;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
