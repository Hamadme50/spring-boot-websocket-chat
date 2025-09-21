package com.app.chatlinks.mysql.dao;


import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class BaseDAO {

//    public String encrypt(String plain) {
//        String b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());
//
//        // Reverse the string
//        String reverse = new StringBuffer(b64encoded).reverse().toString();
//
//        StringBuilder tmp = new StringBuilder();
//        final int OFFSET = 4;
//        for (int i = 0; i < reverse.length(); i++) {
//            tmp.append((char)(reverse.charAt(i) + OFFSET));
//        }
//        return tmp.toString();
//    }
    public String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);
        return encoded;
    }

    public String getUploadFolder (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return path;
    }

    public String cleaner(String data) {
        Document dirtyDoc = Jsoup.parse(data);
        Document cleanDoc = new Cleaner(Whitelist.basicWithImages()).clean(dirtyDoc);
        return cleanDoc.text();
    }
    public String getUID() {
        String ts = String.valueOf(System.currentTimeMillis());
        String rand = UUID.randomUUID().toString();
        return DigestUtils.sha1Hex(ts + rand);
    }

//    public String decrypt(String secret) {
//        StringBuilder tmp = new StringBuilder();
//        final int OFFSET = 4;
//        for (int i = 0; i < secret.length(); i++) {
//            tmp.append((char)(secret.charAt(i) - OFFSET));
//        }
//
//        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
//        return new String(Base64.getDecoder().decode(reversed));
//    }
String calculateTIme(long timeDifferenceMilliseconds) {
    long diffSeconds = timeDifferenceMilliseconds / 1000;
    long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
    long diffHours = timeDifferenceMilliseconds / (60 * 60 * 1000);
    long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
    long diffWeeks = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 7);
    long diffMonths = (long) (timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
    long diffYears = timeDifferenceMilliseconds / ((long)60 * 60 * 1000 * 24 * 365);

    if (diffSeconds < 1) {
        return "less than a second";
    } else if (diffMinutes < 1) {
        return diffSeconds + " seconds";
    } else if (diffHours < 1) {
        return diffMinutes + " minutes";
    } else if (diffDays < 1) {
        return diffHours + " hours";
    } else if (diffWeeks < 1) {
        return diffDays + " days";
    } else if (diffMonths < 1) {
        return diffWeeks + " weeks";
    } else if (diffYears < 1) {
        return diffMonths + " months";
    } else {
        return diffYears + " years";
    }
}

}
