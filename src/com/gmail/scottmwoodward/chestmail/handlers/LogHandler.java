package com.gmail.scottmwoodward.chestmail.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.gmail.scottmwoodward.chestmail.ChestMail;

public class LogHandler {
    public static void logMail(String sender, String recipient, String items, ChestMail plugin){
        try{
            File dataFolder = plugin.getDataFolder();
            if(!dataFolder.exists()){
                dataFolder.mkdir();
            }

            File saveTo = new File(plugin.getDataFolder(), "mail.log");
            if (!saveTo.exists()){
                saveTo.createNewFile();
            }

            PrintWriter pw = new PrintWriter(new FileWriter(saveTo, true));
            
            pw.println(getTimeStamp()+" - "+sender+" sent "+items+" to "+recipient);
            pw.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static String getTimeStamp(){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM/dd/`yyyy HH:mm:ss");
        return format.format(date.getTime());
    }
}
