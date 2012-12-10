package com.gmail.scottmwoodward.chestmail.handlers;

import com.gmail.scottmwoodward.chestmail.ChestMail;

public class ConfigHandler {
    public static ChestMail plugin;
    
    public ConfigHandler(ChestMail plugin){
        ConfigHandler.plugin = plugin;
    }
    
    public static String getString(String param){
        return plugin.getConfig().getString(param);
    }
    
    public static double getDouble(String param){
        return plugin.getConfig().getDouble(param);
    }
    
    public static boolean getBoolean(String param){
        return plugin.getConfig().getBoolean(param);
    }
}
