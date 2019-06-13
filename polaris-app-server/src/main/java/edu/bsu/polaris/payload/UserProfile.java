package edu.bsu.polaris.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long projectCount;

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long projectCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.projectCount = projectCount;
    }

}
