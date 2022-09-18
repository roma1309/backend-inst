package com.instaApp.dto;

import java.util.Set;

public class PostDTO {

    private Long id;
    private String username;
    private String location;
    private String title;
    private String caption;
    private Long likes;
    private Set<String> usersLikes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Set<String> getUsersLikes() {
        return usersLikes;
    }

    public void setUsersLikes(Set<String> usersLikes) {
        this.usersLikes = usersLikes;
    }
}
