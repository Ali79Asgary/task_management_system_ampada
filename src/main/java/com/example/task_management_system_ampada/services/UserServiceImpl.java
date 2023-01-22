package com.example.task_management_system_ampada.services;

import com.example.task_management_system_ampada.exceptions.UserNotFoundException;
import com.example.task_management_system_ampada.exceptions.UsernameOrPasswordIsWrongException;
import com.example.task_management_system_ampada.models.User;
import com.example.task_management_system_ampada.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserById(String id) {
        if (userRepository.findById(id).isPresent())
            return userRepository.findById(id);
        else
            throw new UserNotFoundException();
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user != null)
            return user;
        else
            throw new UserNotFoundException();
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new UsernameOrPasswordIsWrongException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User signupUser(String username, String password) {
        if (!userRepository.existsUserByUsername(username)) {
            User user = new User(username, password);
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty())
            return users;
        else
            throw new UserNotFoundException();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User newUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            return userRepository.save(user);
        }).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
    }

    @Override
    public void deleteUserById(String id) {
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
        else
            throw new UserNotFoundException();
    }
}
