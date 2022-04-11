package org.luvx.map.converter;

import org.luvx.map.entity.User;
import org.luvx.map.entity.UserDto;
import org.luvx.map.entity.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
//@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDto vo2Dto(UserVo vo);

    UserVo dto2Vo(UserDto dto);

    User dto2Entity(UserDto dto);
}