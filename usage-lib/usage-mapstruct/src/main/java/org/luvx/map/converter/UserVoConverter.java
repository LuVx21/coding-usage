package org.luvx.map.converter;

import org.luvx.map.base.BaseConverter;
import org.luvx.map.entity.UserDto;
import org.luvx.map.entity.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
//@Mapper(componentModel = "spring")
public interface UserVoConverter extends BaseConverter<UserVo, UserDto> {
    UserVoConverter INSTANCE = Mappers.getMapper(UserVoConverter.class);

    // UserDto vo2Dto(UserVo vo);
    //
    // UserVo dto2Vo(UserDto dto);
    //
    // User dto2Entity(UserDto dto);
}