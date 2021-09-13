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

/**
 * Provides set of operations with {@link User} entities
 */


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    /**
     * Searches user by specified id
     *
     * @param id - ID of the user to be find
     * @return instance of user with specified id
     * @throws UserNotFoundException if user with specified
     *                               id is not present in the storage
     */
    public UserResponseDto getById(long id) {
        Optional<User> optionalUser = userRepository.getById(id);
        return optionalUser.map(mapper::entityToResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Return a page of users within specified range
     *
     * @param size   -  maximal number of users in one page
     * @param offset - number of user from which page starts
     * @return List of all users located within specified range
     */
    public List<UserResponseDto> getPage(int size, int offset) {
        List<User> page = userRepository.getPage(size, offset);
        return mapper.entitiesToResponses(page);
    }

    public Long getTotalCount() {
        return userRepository.getTotalCount();
    }
}
