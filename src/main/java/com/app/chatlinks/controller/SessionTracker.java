package com.app.chatlinks.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebListener
public class SessionTracker implements ServletContextListener, HttpSessionListener {
    public static ConcurrentMap<String, HttpSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(getClass().getName(), this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {

        sessions.put(event.getSession().getId(), event.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }

    public HttpSession getSessionById(String id) {
        return sessions.get(id);
    }
}