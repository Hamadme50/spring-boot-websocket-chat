package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.GenericDTO;
import com.app.chatlinks.mysql.model.GenericModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IMapper<V extends GenericDTO, M extends GenericModel> {

    M mapToModel(V src);
    M mapToModelClone(V src);
    V mapToDTOClone(M src);
    V mapToDTO(M src);

    /*V mapToDTO(M src, SessionUserInfo sessionUserInfo);
    M mapToModel(V src, SessionUserInfo sessionUserInfo);*/

    List<M> mapToModelList(Collection<V> srcs);
    List<V> mapToDTOList(Collection<M> srcs);
    Set<M> mapToModelSet(Collection<V> srcs);
    Set<V> mapToDTOSet(Collection<M> srcs);
}