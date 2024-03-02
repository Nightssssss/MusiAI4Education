package org.musi.AI4Education.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatSession {
    private List<HashMap<String, String>> messages = new ArrayList<>(); // Store messages

    public void addMessage(HashMap<String, String> message) {
        messages.add(message);
    }

    public List<HashMap<String, String>> getMessages() {
        return messages;
    }
}