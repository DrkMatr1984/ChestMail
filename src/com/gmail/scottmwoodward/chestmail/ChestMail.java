package com.gmail.scottmwoodward.chestmail;

import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.scottmwoodward.chestmail.handlers.CommandHandler;
import com.gmail.scottmwoodward.chestmail.handlers.ConfigHandler;
import com.gmail.scottmwoodward.chestmail.handlers.DBHandler;
import com.gmail.scottmwoodward.chestmail.listeners.PlayerInteractListener;
import com.gmail.scottmwoodward.chestmail.handlers.EconHandler;

public class ChestMail extends JavaPlugin{
    private static CommandHandler handler;
    private Queue<String> settingMailbox;
    
    @Override
    public void onEnable(){
        handler = new CommandHandler(this);
        settingMailbox = new LinkedList<String>();
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getCommand("setmailbox").setExecutor(handler);
        getCommand("sendmail").setExecutor(handler);
        this.saveDefaultConfig();
        new ConfigHandler(this);
        new DBHandler(this);
        new EconHandler(this);
        if(EconHandler.useEcon()){
            if(!EconHandler.vaultInstalled()){
                getLogger().info("Vault missing, disabling economy functions.");
                EconHandler.setUseEcon(false);
            }
        }
    }
    
    public Queue<String> getSettingMailBox(){
        return settingMailbox;
    }
        
}
