package com.epam.esm.service;

import com.epam.esm.dto.UserResponseDto;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.UserMapper;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.excepiton.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public List<UserResponseDto> getAll() {
        List<User> all = userRepository.getAll();
        return mapper.entitiesToResponses(all);
    }

    public UserResponseDto getById(long id) {
        Optional<User> optionalUser = userRepository.getById(id);
        return optionalUser.map(user -> mapper.entityToResponse(user))
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
