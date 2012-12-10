package com.gmail.scottmwoodward.chestmail.commands;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.scottmwoodward.chestmail.ChestMail;
import com.gmail.scottmwoodward.chestmail.handlers.ConfigHandler;
import com.gmail.scottmwoodward.chestmail.handlers.DBHandler;
import com.gmail.scottmwoodward.chestmail.handlers.EconHandler;
import com.gmail.scottmwoodward.chestmail.handlers.LogHandler;

public class SendMailCommand {
    public static void execute(CommandSender sender, String[] args, ChestMail plugin){
        if(sender instanceof Player){
            if(args.length>1){
                sender.sendMessage(ChatColor.YELLOW+"Too many arguments, usage: /sendmail <player>");
                return;
            }
            if(args.length<1){
                sender.sendMessage(ChatColor.YELLOW+"Too few arguments, usage: /sendmail <player>");
                return;
            }
            Player player = (Player) sender;
            Location destination = getMailLocation(player, args[0], plugin);
            if(destination != null){
                sendMail(destination, player, args[0], plugin);
            }

        }
        else{
            sender.sendMessage("You must be logged in as a player to send mail.");
        }
    }

    public static Location getMailLocation(Player sender, String recipient, ChestMail plugin){
        if(DBHandler.hasMailbox(recipient)){
            double x = DBHandler.getComponent(recipient, "x");
            double y = DBHandler.getComponent(recipient, "y");
            double z = DBHandler.getComponent(recipient, "z");
            String worldname = DBHandler.getWorld(recipient);
            if(x==0||y==0||z==0||worldname.equalsIgnoreCase("")){
                sender.sendMessage(ChatColor.YELLOW+"No mailbox found for player: "+recipient);
                return null;
            }
            World world = plugin.getServer().getWorld(worldname);
            return new Location(world, x, y, z);
        }
        else{
            sender.sendMessage(ChatColor.YELLOW+"No mailbox found for player: "+recipient);
            return null;
        }

    }

    public static void sendMail(Location destination, Player player, String recipient, ChestMail plugin){
        Inventory inventory;
        Block mailbox = destination.getBlock();
        if(isChest(mailbox)){
            inventory = ((Chest)destination.getBlock().getState()).getBlockInventory();
            if(!hasEmptySlot(inventory)){
                player.sendMessage(ChatColor.YELLOW+recipient+"'s mailbox is full");
                return; 
            }
            if(player.getItemInHand().getTypeId()==0){
                player.sendMessage(ChatColor.YELLOW+"You don't have anything to mail!");
                return;
            }
            Double cost = ConfigHandler.getDouble("FlatCostToSendMail")+(player.getItemInHand().getAmount()*ConfigHandler.getDouble("CostPerItemSent"));
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            cost =  Double.valueOf(twoDForm.format(cost));
            if(EconHandler.hasEnoughMoney(player, cost)){
                EconHandler.takePayment(player, cost, "for postage");
                mailPlayerItem(player.getItemInHand(), inventory, plugin, player, recipient);
                takePlayerItem(player, recipient, plugin); 
            }
            else{
                player.sendMessage(ChatColor.YELLOW+"You do not have enough money to mail "+getReadableItemStack(player.getItemInHand())+ChatColor.GREEN+". (Need "+cost+" "+EconHandler.getCurrencyContext(cost)+")");
            }
        }
        else{
            player.sendMessage(ChatColor.YELLOW+"No mailbox found for player: "+recipient);
        }
    }

    public static void takePlayerItem(Player player, String recipient, ChestMail plugin){

        player.sendMessage(ChatColor.YELLOW+"You have sent "+ChatColor.GREEN+getReadableItemStack(player.getItemInHand())+ChatColor.YELLOW+ " to "+ recipient);
        LogHandler.logMail(player.getName(), recipient, getReadableItemStack(player.getItemInHand()), plugin);
        player.setItemInHand(null);
    }

    public static void mailPlayerItem(ItemStack mail, Inventory box, ChestMail plugin, Player player, String recipient){
        box.addItem(mail);
        for (Player p: plugin.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(recipient)){
                p.sendMessage(ChatColor.YELLOW+"You have received "+ChatColor.GREEN+getReadableItemStack(player.getItemInHand())+ChatColor.YELLOW+ " from "+ player.getName());
            }
        }
    }

    public static boolean isChest(Block mailbox){
        return(mailbox.getTypeId() == 54);
    }

    public static boolean hasEmptySlot(Inventory inventory){
        if(inventory.firstEmpty() == -1){
            return false;
        }
        return true;
    }

    public static String getReadableItemStack(ItemStack items){
        String readable = items.getType().toString().replaceAll("[0-9]*$", "");
        readable = readable.replaceAll("\\(", "");
        readable = readable.replaceAll("\\)", "");
        readable = readable.replaceAll("_", " ");
        readable = readable.toLowerCase();
        return String.valueOf(items.getAmount())+" "+readable;
    }

}
