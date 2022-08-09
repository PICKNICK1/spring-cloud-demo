package com.lk.upms.api.convert;


import com.lk.upms.api.dto.UserDTO;
import com.lk.upms.api.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserConvert {

    PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Mapping(target = "password",expression = "java(ENCODER.encode(userDTO.getPassword()))")
    SysUser userDTO2User(UserDTO userDTO);
}
