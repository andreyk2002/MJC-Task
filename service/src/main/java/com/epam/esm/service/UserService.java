package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.mappers.UserMapper;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.excepiton.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final PageLimiter pageLimiter;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public List<UserResponseDto> getAll() {
        List<User> all = userRepository.getAll();
        return mapper.entitiesToResponses(all);
    }

    public UserResponseDto getById(long id) {
        Optional<User> optionalUser = userRepository.getById(id);
        return optionalUser.map(mapper::entityToResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserResponseDto> getPage(int size, int offset) {
        int limitedSize = pageLimiter.limitSize(size);
        List<User> page = userRepository.getPage(limitedSize, offset);
        return mapper.entitiesToResponses(page);
    }
}
