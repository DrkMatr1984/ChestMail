package com.gmail.scottmwoodward.chestmail.handlers;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.gmail.scottmwoodward.chestmail.ChestMail;



public class EconHandler {
    private static ChestMail plugin;
    private static boolean useEcon;
    private static Economy econ;

    public EconHandler(ChestMail plugin){
        EconHandler.plugin = plugin;
        setUseEcon(plugin.getConfig().getBoolean("EconomyEnabled"));
    }

    public static boolean useEcon(){
        return useEcon;
    }

    public static boolean vaultInstalled(){
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void setUseEcon(boolean use){
        useEcon = use;
    }

    public static boolean hasEnoughMoney(Player player, double cost){
        if(!useEcon){
            return true;
        }
        if(econ.getBalance(player.getName())>=cost){
            return true;
        }
        return false;
    }

    public static void takePayment(Player player, double cost, String reason){
        if(useEcon){
            econ.withdrawPlayer(player.getName(), cost);
            player.sendMessage(ChatColor.YELLOW+"You have been charged "+ChatColor.GREEN+String.valueOf(cost)+" "+getCurrencyContext(cost)+ChatColor.YELLOW+" "+reason);
        }
    }

    public static String getCurrencyContext(double cost){
        if(cost == 1){
            return econ.currencyNameSingular();
        }
        else{
            return econ.currencyNamePlural();
        }
    }
  
}
