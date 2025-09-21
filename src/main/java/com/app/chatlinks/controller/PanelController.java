package com.app.chatlinks.controller;


import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.mysql.dao.PanelDAO;
import com.app.chatlinks.dto.ChatRoomDTO;
import com.app.chatlinks.dto.CustomerDTO;
import com.app.chatlinks.dto.RankDTO;
import com.app.chatlinks.dto.panel.DashboardDTO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/panel")
public class PanelController extends BaseController{

    @Autowired
    PanelDAO panelDAO;

    @RequestMapping("/app")
    public String panel( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);

            DashboardDTO dashboardDTO = panelDAO.getDashboardData(customerDTO.getId());
            model.addAttribute("dashboard",dashboardDTO);
            return "panel/app";
        }
        catch (Exception e){
            e.printStackTrace();
            return "panel/signin";
        }
    }

    @RequestMapping("/network")
    public String network( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            if(customerDTO.getOwner().equals("Y")){
                model.addAttribute("data",customerDTO);

                model.addAttribute("networks",panelDAO.getNetworkData());
                return "panel/network";
            }
            else{

                model.addAttribute("data",customerDTO);
                DashboardDTO dashboardDTO = panelDAO.getDashboardData(customerDTO.getId());
                model.addAttribute("dashboard",dashboardDTO);
                return "panel/app";
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return "panel/signin";
        }
    }

    @RequestMapping("/signup")
    public String signup( ModelMap model) {

        return "panel/signup";
    }
    @RequestMapping("/logout")
    public String logout( ModelMap model) {
        session.setAttribute(GlobalConstants.SESSION.CUSTOMER, null);
        return "panel/signin";
    }

    @RequestMapping("/changepassword")
    public String changepassword( ModelMap model) {
        try{
            CustomerDTO customerDTO = getCurrentCustomer();
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error","Error in changing password please sign in again");
            return "panel/signin";
        }
        return "panel/changepassword";
    }

    @RequestMapping("/signin")
    public String signin( ModelMap model) {

        return "panel/signin";
    }

    @RequestMapping("/recover")
    public String recover( ModelMap model) {

        return "panel/recover";
    }

    @RequestMapping(value = "/recoverpassword", method = RequestMethod.POST)
    public String recoverpassword(String email ,ModelMap model) {
        try {
            if (email != null && (!email.equals(""))) {
                if (panelDAO.checkCustomerEmailExist(email)) {
                    panelDAO.recoverCustomerPass(email);
                    model.addAttribute("ok", "Password send to " + email + " check inbox or spam");
                    return "panel/signin";
                }
            }
            else{
                model.addAttribute("error", "Please fill details correctly");
                return "panel/recover";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("error", "No account exist with "+email);
        return "panel/recover";
    }

    @RequestMapping(value = "/applypassword", method = RequestMethod.POST)
    public String applypassword(CustomerDTO data, ModelMap model) {

        try{
            if(data.getPassword() != null && data.getOldPassword() != null && !(data.getPassword().equals("")) && !(data.getOldPassword().equals(""))) {
                CustomerDTO customerDTO = getCurrentCustomer();
                data.setId(customerDTO.getId());
                int result = panelDAO.changePassword(data);
                session.invalidate();
                if (result == 0) {
                    model.addAttribute("error", "Error in changing password beacuse password did not matched");
                    return "panel/signin";
                }
                model.addAttribute("ok", "Password changed sucessfully now please sign in again");
                return "panel/signin";
            }
            else{
                model.addAttribute("error", "Error in changing password please enter details correctly");
                return "panel/signin";
            }

        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error","Error in changing password please sign in again");
            return "panel/signin";
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(CustomerDTO customerDTO, ModelMap model) {

        if(customerDTO != null && customerDTO.getName() != null && !(customerDTO.getName().equals("")) && customerDTO.getEmail() != null  && !(customerDTO.getEmail().equals("")) && customerDTO.getPassword() != null && !(customerDTO.getPassword().equals("") ) ){
            try {
                if(panelDAO.checkCustomerEmailExist(customerDTO.getEmail())){
                    model.addAttribute("error","Already account registered with "+customerDTO.getEmail());
                    return "panel/signup";
                }
                CustomerDTO data = panelDAO.saveCustomer(customerDTO);
                if(data.getRequestStatus().equals(1)){
                    model.addAttribute("error","Sign up error account already registered with this email");
                    return "panel/signup";
                }
                model.addAttribute("data",data);
                DashboardDTO dashboardDTO = new DashboardDTO();
                model.addAttribute("dashboard",dashboardDTO);
                Gson gson = new Gson();
                String customerSession = gson.toJson(data);
                session.setAttribute(GlobalConstants.SESSION.CUSTOMER, customerSession);

                return "panel/app";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error","Error while sign up try again later");
                return "panel/signup";
            }

        }
        else{
            return "panel/signup";
        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(CustomerDTO customerDTO, ModelMap model) {

        if(customerDTO != null && customerDTO.getEmail() != null && customerDTO.getPassword() != null ){
            try {
                CustomerDTO data = panelDAO.loginCustomer(customerDTO);
                model.addAttribute("data",data);
                DashboardDTO dashboardDTO = panelDAO.getDashboardData(data.getId());
                model.addAttribute("dashboard",dashboardDTO);
                Gson gson = new Gson();
                String customerSession = gson.toJson(data);
                session.setAttribute(GlobalConstants.SESSION.CUSTOMER, customerSession);
                return "panel/app";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error","Wrong email or password");
                return "panel/signin";
            }

        }
        else{
            return "panel/signin";
        }

    }

    @RequestMapping(value = "/newchatroom", method = RequestMethod.POST)
    public String newchatroom(ChatRoomDTO chatRoomDTO, ModelMap model) {

        if(chatRoomDTO != null && chatRoomDTO.getName() != null && !(chatRoomDTO.getName().equals("")) && chatRoomDTO.getDomain() != null && !(chatRoomDTO.getDomain().equals(""))
           && chatRoomDTO.getNickname() != null && !(chatRoomDTO.getNickname().equals("")) && chatRoomDTO.getPassword() != null && !(chatRoomDTO.getPassword().equals(""))){
            try {
                CustomerDTO data = getCurrentCustomer();
                if(panelDAO.isDuplicateChatRoom(data.getId())){
                    model.addAttribute("data",data);
                    model.addAttribute("error","There is error in your chat room please contact us via skype mentioned in support section");
                    return "panel/makechatroom";
                }
                chatRoomDTO.setCustomerId(data.getId());
                Long chatRoomId = panelDAO.newChatRoom(chatRoomDTO,data.getEmail());
                if(chatRoomId == null){
                    model.addAttribute("data",data);
                    model.addAttribute("error","There is some error at our end please try again later");
                    return "panel/makechatroom";
                }
                model.addAttribute("chatRoomId",chatRoomId);
                return "panel/mychatroom";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error","There is some error at our end please try again later");
                return "panel/makechatroom";
            }

        }
        else{
            model.addAttribute("error","Please fill all details correctly");
            return "panel/makechatroom";
        }

    }

    @RequestMapping("/chatroom")
    public String chatroom( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            Long chatRoomId = panelDAO.getMyChatRoom(customerDTO.getId());
            model.addAttribute("chatRoomId",chatRoomId);
            return "panel/mychatroom";
        }
        catch (Exception e){
            e.printStackTrace();
            try{
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                return "panel/makechatroom";
            }
            catch (Exception r){
                return "panel/signin";
            }

        }
    }
    @RequestMapping("/settings")
    public String settings( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
            model.addAttribute("chatroom",chatroom);
            return "panel/settings";
        }
        catch (Exception e){
            e.printStackTrace();
            try{
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                return "panel/panelerror";
            }
            catch (Exception r){
                return "panel/signin";
            }

        }
    }
    @RequestMapping("/support")
    public String support( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            return "panel/support";
        }
        catch (Exception e){
                return "panel/signin";
        }
    }
    @RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
    public String updateSettings(ChatRoomDTO chatRoomDTO, ModelMap model) {

        if(chatRoomDTO != null && chatRoomDTO.getName() != null && !(chatRoomDTO.getName().equals("")) && chatRoomDTO.getDomain() != null && !(chatRoomDTO.getDomain().equals(""))
                && chatRoomDTO.getTheme() != null && !(chatRoomDTO.getTheme().equals("")) && chatRoomDTO.getAntiSpam() != null && !(chatRoomDTO.getAntiSpam().equals(""))  && chatRoomDTO.getDesign() != null && !(chatRoomDTO.getDesign().equals(""))){
            try {
                CustomerDTO customerDTO = getCurrentCustomer();
                ChatRoomDTO data = panelDAO.updateSettings(chatRoomDTO,customerDTO.getId());
                model.addAttribute("data",customerDTO);
                model.addAttribute("chatroom",data);
                model.addAttribute("msg", "Saved Sucessfully Changes Will Take 5 Min To Update");

                return "panel/settings";
            } catch (Exception e) {
                e.printStackTrace();
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
                model.addAttribute("chatroom",chatroom);
                model.addAttribute("error","There is some error at our end please try again later");
                return "panel/settings";
            }

        }
        else{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
            model.addAttribute("chatroom",chatroom);
            model.addAttribute("error","Please fill all details correctly");
            return "panel/settings";
        }

    }
    @RequestMapping(value = "/updateRank", method = RequestMethod.POST)
    public String updateRank(RankDTO data, ModelMap model) {

        if(data.getName() != null && (!data.getName().equals("")) && data.getIcon() != null && (!data.getIcon().equals("")) && data.getChangeNick() != null && (!data.getChangeNick().equals(""))  && data.getDeleteMsg() != null && (!data.getDeleteMsg().equals(""))
                && data.getKick() != null && (!data.getKick().equals("")) && data.getMute() != null && (!data.getMute().equals("")) && data.getSpam() != null && (!data.getSpam().equals("")) && data.getBan() != null && (!data.getBan().equals(""))
        ){
            try {
                CustomerDTO customerDTO = getCurrentCustomer();
                ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
                model.addAttribute("chatroom",chatroom);
                RankDTO updateRank = panelDAO.updateRank(data,chatroom);
                model.addAttribute("data",customerDTO);
                model.addAttribute("msg", "Saved Sucessfully");
                model.addAttribute("rankData", updateRank);
                model.addAttribute("rankId", updateRank.getId());

                return "panel/ranks";
            } catch (Exception e) {
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
                model.addAttribute("chatroom",chatroom);
                List<RankDTO> rankList =  panelDAO.getRankList(chatroom);
                model.addAttribute("ranks",rankList);
                model.addAttribute("rankId", data.getId());
                model.addAttribute("error","There is some error at our end please try again later");
                return "panel/ranks";
            }

        }
        else{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
            model.addAttribute("chatroom",chatroom);
            List<RankDTO> rankList =  panelDAO.getRankList(chatroom);
            model.addAttribute("ranks",rankList);
            model.addAttribute("rankId", data.getId());
            model.addAttribute("error","Please fill details correctly");
            return "panel/ranks";
        }

    }
    @RequestMapping(value = "/selectPRank", method = RequestMethod.POST)
    public String selectPRank(Long rank, ModelMap model) {

        try{
            if(rank != null){
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
                RankDTO rankDTO = panelDAO.getRankData(rank,chatroom);
                model.addAttribute("rankData",rankDTO);
                model.addAttribute("rankId",rank);
                List<RankDTO> rankList =  panelDAO.getRankList(chatroom);
                model.addAttribute("ranks",rankList);
                return "panel/ranks";
            }
            else{
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
                model.addAttribute("chatroom",chatroom);
                List<RankDTO> rankList =  panelDAO.getRankList(chatroom);
                model.addAttribute("ranks",rankList);
                model.addAttribute("error","Please fill all details correctly");
                return "panel/ranks";
            }

        }
        catch (Exception e){
            e.printStackTrace();
            try{
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                return "panel/panelerror";
            }
            catch (Exception r){
                return "panel/signin";
            }

        }

    }
    @RequestMapping("/ranks")
    public String ranks( ModelMap model) {

        try{
            CustomerDTO customerDTO = getCurrentCustomer();
            model.addAttribute("data",customerDTO);
            ChatRoomDTO chatroom = panelDAO.getMyChatRoomData(customerDTO.getId());
            model.addAttribute("chatroom",chatroom);
            List<RankDTO> rankList =  panelDAO.getRankList(chatroom);
            model.addAttribute("ranks",rankList);
            return "panel/ranks";
        }
        catch (Exception e){
            e.printStackTrace();
            try{
                CustomerDTO customerDTO = getCurrentCustomer();
                model.addAttribute("data",customerDTO);
                return "panel/panelerror";
            }
            catch (Exception r){
                return "panel/signin";
            }

        }
    }

}
