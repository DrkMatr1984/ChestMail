package com.gmail.scottmwoodward.chestmail.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.scottmwoodward.chestmail.ChestMail;
import com.gmail.scottmwoodward.chestmail.commands.SendMailCommand;
import com.gmail.scottmwoodward.chestmail.commands.SetMailboxCommand;

public class CommandHandler implements CommandExecutor{
    private ChestMail plugin;

    public CommandHandler(ChestMail plugin){
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("setmailbox")){
            SetMailboxCommand.execute(sender,plugin);
         }
        else if(cmd.getName().equalsIgnoreCase("sendmail")){
            SendMailCommand.execute(sender,args,plugin);
         }
        return true;
    }
}
