package org.luvx.map.base;

import java.util.List;
import java.util.Map;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;

public interface BaseConverter<FROM, TO> {

    TO from2To(FROM from);

    @InheritInverseConfiguration(name = "from2To")
    FROM to2From(TO to);

    @InheritConfiguration(name = "from2To")
    List<TO> mulFrom2To(List<FROM> froms);

    @InheritConfiguration(name = "to2From")
    List<FROM> mulTo2From(List<TO> tos);

    @InheritConfiguration(name = "to2From")
    void updateFrom(TO to, @MappingTarget FROM from);

    @InheritConfiguration(name = "from2To")
    void updateTo(FROM from, @MappingTarget TO to);

    FROM map2From(Map<String, Object> map);

    TO map2To(Map<String, Object> map);
}