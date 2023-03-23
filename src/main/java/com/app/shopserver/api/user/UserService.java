package com.app.shopserver.api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Mono<UserModel> register(UserModel userModel) {
        String encryptedPassword = passwordEncoder.encode(userModel.password());
        var savedUser = userModel.generateEncryptedCopy(encryptedPassword);
        return userRepository.save(savedUser);
    }

}
