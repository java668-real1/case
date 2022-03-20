package com.janey668.c1;

import lombok.Builder;

@Builder
public class Message {

    private int id;
    private String name;

    public static void main(String[] args) {
        Message test = Message.builder()
                .id(1)
                .name("test")
                .build();
    }
}
