package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.UserDTO;
import com.app.chatlinks.mysql.model.Rank;
import com.app.chatlinks.mysql.model.User;


public class UserMapper extends AbstractMapper<UserDTO, User>{
    @Override
    public User mapToModel(UserDTO src) {
        User dest = null;
        if(src != null){
            dest = new User();
            dest.setId(src.getId());
            dest.setUserName(src.getUserName());
            dest.setJoinDate(src.getJoinDate());
            dest.setStatus(src.getStatus());
            dest.setActivityDate(src.getActivityDate());
            dest.setBirthDate(src.getBirthDate());
            dest.setGender(src.getGender());
            dest.setDp(src.getDp());
            dest.setRj(src.getRj());
            dest.setIp(src.getIp());
            dest.setSpam(src.getSpam());
            dest.setBan(src.getBan());
            dest.setTweet(src.getTweet());
            dest.setAbout(src.getAbout());
            dest.setNameColor(src.getNameColor());
            dest.setKick(src.getKick());
            dest.setEmail(src.getEmail());
            dest.setPoints(src.getPoints());
            dest.setFont(src.getFont());
            dest.setBold(src.getBold());
            dest.setTextColor(src.getTextColor());
        }
        return dest;
    }

    @Override
    public UserDTO mapToDTO(User src) {
        UserDTO dest = null;
        if(src != null){
            dest = new UserDTO();
            dest.setId(src.getId());
            dest.setUserName(src.getUserName());
            dest.setJoinDate(src.getJoinDate());
            dest.setStatus(src.getStatus());
            dest.setActivityDate(src.getActivityDate());
            dest.setBirthDate(src.getBirthDate());
            dest.setGender(src.getGender());
            dest.setDp(src.getDp());
            dest.setRj(src.getRj());
            dest.setIp(src.getIp());
            dest.setSpam(src.getSpam());
            dest.setBan(src.getBan());
            dest.setTweet(src.getTweet());
            dest.setAbout(src.getAbout());
            dest.setNameColor(src.getNameColor());
            dest.setKick(src.getKick());
            dest.setEmail(src.getEmail());
            dest.setPoints(src.getPoints());
            dest.setFont(src.getFont());
            dest.setBold(src.getBold());
            dest.setTextColor(src.getTextColor());

            dest.setChatRoomId(src.getChatRoom().getId());
            dest.setRadio(src.getChatRoom().getRadio());
            Rank rank = src.getRank();
            dest.setRankId(rank.getId());
            RankMapper rankMapper = new RankMapper();
            dest.setRank(rankMapper.mapToDTO(rank));
        }
        return dest;
    }
}
