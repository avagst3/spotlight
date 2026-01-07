package com.spotlight.back.spotlight.models.auth;

import com.spotlight.back.spotlight.models.entities.User;
import lombok.*;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal {

    public CustomPrincipal (User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
    }

    private UUID id;
    private String name;
    private String username;
}
