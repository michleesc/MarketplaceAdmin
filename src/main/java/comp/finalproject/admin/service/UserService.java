package comp.finalproject.admin.service;

import comp.finalproject.admin.dto.UserDto;
import comp.finalproject.admin.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}