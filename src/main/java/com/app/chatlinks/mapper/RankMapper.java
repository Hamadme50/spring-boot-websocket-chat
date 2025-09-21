package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.RankDTO;
import com.app.chatlinks.mysql.model.Rank;

public class RankMapper extends AbstractMapper<RankDTO, Rank>{
    @Override
    public Rank mapToModel(RankDTO src) {
        Rank dest = null;
        if(src != null){
            dest = new Rank();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setCode(src.getCode());
            dest.setIcon(src.getIcon());
            dest.setKick(src.getKick());
            dest.setBan(src.getBan());
            dest.setSpam(src.getSpam());
            dest.setMute(src.getMute());
            dest.setChangeNick(src.getChangeNick());
            dest.setDeleteMsg(src.getDeleteMsg());

        }
        return dest;
    }

    @Override
    public RankDTO mapToDTO(Rank src) {
        RankDTO dest = null;
        if(src != null){
            dest = new RankDTO();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setCode(src.getCode());
            dest.setIcon(src.getIcon());
            dest.setKick(src.getKick());
            dest.setBan(src.getBan());
            dest.setSpam(src.getSpam());
            dest.setMute(src.getMute());
            dest.setChangeNick(src.getChangeNick());
            dest.setDeleteMsg(src.getDeleteMsg());

            dest.setChatRoomId(src.getChatRoom().getId());

        }
        return dest;
    }
}
