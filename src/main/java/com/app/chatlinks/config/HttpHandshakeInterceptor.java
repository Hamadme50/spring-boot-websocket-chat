package com.app.chatlinks.config;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
       /* if (request instanceof ServletServerHttpRequest) {
            try {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                HttpSession session = (HttpSession) servletRequest.getServletRequest().getSession();
                attributes.put("sessionId", session.getId());
                String[] token = ((ServletServerHttpRequest) request).getServletRequest().getParameterMap().get("token");
                String chatroomid = token[0];

                attributes.put(GlobalConstants.SESSION.USER, session.getAttribute(GlobalConstants.SESSION.USER + "-" + chatroomid));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;*/


        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");
            if (token != null) {
                // Store the token in session attributes
                attributes.put("token", token);
            } else {
                // Handle missing token (e.g., reject handshake)
                return false; // Reject the handshake if token is required
            }
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
    }
}