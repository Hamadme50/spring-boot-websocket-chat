package com.app.chatlinks.config;

import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.*;
import java.util.*;

@Configuration
public class HttpSessionConfig {

    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public List<HttpSession> getActiveSessions() {
        return new ArrayList<>(sessions.values());
    }

    public HttpSession getActiveSessionsById(String id) {
        return sessions.get(id);

    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Autowired
            ChatDAO chatDAO;

            @Override
            public void sessionCreated(HttpSessionEvent hse) {
                sessions.put(hse.getSession().getId(), hse.getSession());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent hse) {

                HttpSession httpSession = getActiveSessionsById(hse.getSession().getId());
                if(httpSession != null) {
                    Enumeration<String> attributes = httpSession.getAttributeNames();
                    while (attributes.hasMoreElements()) {
                        String attribute = (String) attributes.nextElement();
                        try {
                            UserDTO userDTO = (UserDTO) httpSession.getAttribute(attribute);
                            if (userDTO.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) {
                                chatDAO.expireGuest(userDTO.getId());
                            }
                        } catch (Exception e) {
                        }
                    }

                    sessions.remove(hse.getSession().getId());
                }
            }
        };
    }
}