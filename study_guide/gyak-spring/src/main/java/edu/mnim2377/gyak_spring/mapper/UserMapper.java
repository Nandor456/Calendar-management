package edu.mnim2377.gyak_spring.mapper;

import edu.mnim2377.gyak_spring.data.UserData;
import edu.mnim2377.gyak_spring.dto.CreateUserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserData toDto(CreateUserRequestDTO user) {
        if (user == null) {
            return null;
        }
        UserData dto = new UserData();
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        return dto;
    }
}
