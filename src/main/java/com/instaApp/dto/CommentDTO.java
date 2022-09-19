package com.instaApp.dto;

import javax.validation.constraints.NotEmpty;

public class CommentDTO {
    private Long id;
    @NotEmpty
    private String message;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
