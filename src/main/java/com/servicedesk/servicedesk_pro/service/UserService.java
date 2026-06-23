package com.servicedesk.servicedesk_pro.service;

import com.servicedesk.servicedesk_pro.dto.CreateUserRequest;
import com.servicedesk.servicedesk_pro.dto.UpdateUserRequest;
import com.servicedesk.servicedesk_pro.dto.UserResponse;
import com.servicedesk.servicedesk_pro.exception.UserNotFoundException;
import com.servicedesk.servicedesk_pro.model.User;
import com.servicedesk.servicedesk_pro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email Already Exists");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());

        return mapToUserResponse(userRepository.save(user));
    }

    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
        return "User deleted successfully";
    }

    private UserResponse mapToUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
