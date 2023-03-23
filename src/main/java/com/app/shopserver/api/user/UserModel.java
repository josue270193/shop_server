package com.app.shopserver.api.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public record UserModel(
        @Id String id,
        String username,
        String name,
        String lastname,
        String password,
        boolean isEnabled
) {

    public UserModel generateCreationCopy() {
        return new UserModel(null, this.username, this.name, this.lastname, this.password, true);
    }
    public UserModel generateEncryptedCopy(String password) {
        return new UserModel(null, this.username, this.name, this.lastname, password, this.isEnabled);
    }
}
