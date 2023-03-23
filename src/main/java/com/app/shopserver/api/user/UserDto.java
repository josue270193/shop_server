package com.app.shopserver.api.user;

public record UserDto(
        String username,
        String name,
        String lastname,
        String jwt,
        boolean isEnabled
) {

    public UserDto(UserModel userModel, String jwt){
        this(userModel.username(), userModel.name(), userModel.lastname(), jwt, userModel.isEnabled());
    }
}
