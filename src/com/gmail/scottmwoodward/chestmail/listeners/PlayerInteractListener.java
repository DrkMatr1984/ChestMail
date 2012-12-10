package com.gmail.scottmwoodward.chestmail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.gmail.scottmwoodward.chestmail.handlers.ConfigHandler;
import com.gmail.scottmwoodward.chestmail.handlers.DBHandler;
import com.gmail.scottmwoodward.chestmail.handlers.EconHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.scottmwoodward.chestmail.ChestMail;

public class PlayerInteractListener implements Listener{
    ChestMail plugin;
    public PlayerInteractListener(ChestMail plugin) {
        super();
        this.plugin = plugin;
    }

    @EventHandler
    public void punchingChest(PlayerInteractEvent event){
        if(plugin.getSettingMailBox().contains(event.getPlayer().getName())){
            if(event.getAction() == Action.RIGHT_CLICK_AIR){
                return;
            }
            plugin.getSettingMailBox().remove(event.getPlayer().getName());
            Block clicked = event.getClickedBlock();
            if(clicked.getType() == Material.CHEST){
                if(!EconHandler.hasEnoughMoney(event.getPlayer(), ConfigHandler.getDouble("CostToSetMailbox"))){
                    event.getPlayer().sendMessage(ChatColor.YELLOW+"You do not have enough money to set your mailbox");
                    return;
                }
                EconHandler.takePayment(event.getPlayer(), ConfigHandler.getDouble("CostToSetMailbox"), "to set your mailbox");
                if(DBHandler.hasMailbox(event.getPlayer().getName())){
                    DBHandler.updateMailbox(event.getPlayer().getName(),clicked.getX(),clicked.getY(),clicked.getZ(),clicked.getWorld().getName());
                    event.getPlayer().sendMessage(ChatColor.YELLOW+"You have updated your mailbox");
                }
                else{
                    DBHandler.setMailbox(event.getPlayer().getName(),clicked.getX(),clicked.getY(),clicked.getZ(),clicked.getWorld().getName());
                    event.getPlayer().sendMessage(ChatColor.YELLOW+"You have created your mailbox");
                }
            }
            else{
                event.getPlayer().sendMessage(ChatColor.YELLOW+"You may only designate a chest as your Mailbox");
            }
            
        }
    }
}
