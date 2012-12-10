package com.gmail.scottmwoodward.chestmail.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.scottmwoodward.chestmail.ChestMail;
import com.gmail.scottmwoodward.chestmail.handlers.ConfigHandler;
import com.gmail.scottmwoodward.chestmail.handlers.EconHandler;

public class SetMailboxCommand {
    public static void execute(CommandSender sender, ChestMail plugin){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!EconHandler.hasEnoughMoney(player, ConfigHandler.getDouble("CostToSetMailbox"))){
                player.sendMessage(ChatColor.YELLOW+"You do not have enough money to set your mailbox");
                return;
            }
            plugin.getSettingMailBox().add(player.getName());
            player.sendMessage(ChatColor.YELLOW+"Left click a chest to set it as your mailbox, this will overwrite any previous mailbox selections");
            player = null;
        }
        else{
            sender.sendMessage("You must be logged in as a player to set a mailbox.");
        }
    }
}
