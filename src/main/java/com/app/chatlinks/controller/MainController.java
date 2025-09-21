package com.app.chatlinks.controller;

import com.app.chatlinks.config.GlobalConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController extends BaseController{
    @RequestMapping("/")
    @Cacheable("main")
    public String main(ModelMap model, HttpServletRequest requestasd) {
        model.addAttribute("cache", GlobalConstants.BASE.CACHE);
        return "index";

    }

}
