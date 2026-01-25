package edu.mnim2377.gyak_spring.service;

import edu.mnim2377.gyak_spring.data.UserData;
import edu.mnim2377.gyak_spring.dto.CreateUserRequestDTO;
import edu.mnim2377.gyak_spring.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

}
