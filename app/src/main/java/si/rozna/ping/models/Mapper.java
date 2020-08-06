package si.rozna.ping.models;

import si.rozna.ping.models.dto.GroupDTO;

public class Mapper {

    public static Group mapGroupFromDTO(GroupDTO dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        group.setOwnerId(dto.getOwnerId());
        return group;
    }

    public static GroupDTO mapGroupToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setOwnerId(group.getOwnerId());
        return dto;
    }

}
