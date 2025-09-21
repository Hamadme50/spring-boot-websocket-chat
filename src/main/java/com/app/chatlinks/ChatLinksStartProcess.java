package com.app.chatlinks;

import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.mysql.dao.GuestClear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Timer;

@Component
public class ChatLinksStartProcess implements ApplicationRunner {

    @Autowired
    ChatDAO chatDAO;
    public static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            chatDAO.removeAllUserSessionDb();
            Timer time = new Timer();
            GuestClear guestClear = new GuestClear(chatDAO);
            time.schedule(guestClear, 0, 95000000); // every 15 min

//            ClearUser clearUser = new ClearUser(chatDAO);
//            time.schedule(clearUser, 0, 95000000);

            /*ClearStay clearStay = new ClearStay();
            time.schedule(clearStay, 0, 95000000);*/ // every 26 hours


            /*File[] files1 = getResourceFolderFiles("static/content/emojis/easy");
            for(File file :files1){
                String name = file.getName();
                String[] emoji = name.split("\\.");
                String emo = "  emo2[':"+emoji[0]+"'] ='<img class=\"emo_chat\" src=\"/content/emojis/easy/"+file.getName()+"\" data=\":"+emoji[0]+"\" >';";
                System.out.println(emo);

            }*/
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
