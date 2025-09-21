package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.IpBannedDTO;
import com.app.chatlinks.mysql.model.IpBanned;

public class IpBannedMapper extends AbstractMapper<IpBannedDTO, IpBanned>{
    @Override
    public IpBanned mapToModel(IpBannedDTO src) {
        IpBanned dest = null;
        if(src != null){
            dest = new IpBanned();
            dest.setId(src.getId());
            dest.setIp(src.getIp());
            dest.setUsername(src.getUsername());
            dest.setDate(src.getDate());
        }
        return dest;
    }

    @Override
    public IpBannedDTO mapToDTO(IpBanned src) {
        IpBannedDTO dest = null;
        if(src != null){
            dest = new IpBannedDTO();
            dest.setId(src.getId());
            dest.setIp(src.getIp());
            dest.setDate(src.getDate());
            dest.setUsername(src.getUsername());

            dest.setChatRoomId(src.getChatRoom().getId());
        }
        return dest;
    }
}
