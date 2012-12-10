package com.gmail.scottmwoodward.chestmail.handlers;

import java.sql.ResultSet;

import com.gmail.scottmwoodward.chestmail.ChestMail;

import lib.PatPeter.SQLibrary.SQLite;

public class DBHandler {

    private static SQLite sqlite;
    private ChestMail plugin;

    public DBHandler(ChestMail plugin){
        this.plugin = plugin;
        sqlConnection();
        tableCheck();
    }

    public static boolean hasMailbox(String player){
        String lookup = "SELECT * FROM mailboxes WHERE playername='"+player.toLowerCase()+"'";
        ResultSet r = sqlite.query(lookup);
        try{
            if(r.next()){
                r.close();
                return true;
            }
            r.close();
            return false;
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static void setMailbox(String player, double x, double y, double z, String world){
        String insert = "INSERT INTO mailboxes(playername, world, x, y, z) VALUES('"+player.toLowerCase()+"', '"+world+"', "+x+", "+y+", "+z+")";
        ResultSet r = sqlite.query(insert);
        try{
            if(r!=null){
                r.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void updateMailbox(String player, double x, double y, double z, String world){
        String update = "UPDATE mailboxes set world='"+world+"', playername='"+player.toLowerCase()+"', x="+String.valueOf(x)+", y="+String.valueOf(y)+", z="+String.valueOf(z);
        ResultSet r = sqlite.query(update);
        try{
            if(r!=null){
                r.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static double getComponent(String player, String component){
        String select = "SELECT "+component+" FROM mailboxes WHERE playername='"+player.toLowerCase()+"'";
        ResultSet r = sqlite.query(select);
        try{
            if(r.next()){
                 double value = r.getDouble(component);
                 r.close();
                 return value;
            }
            else{
                r.close();
                return 0;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public static String getWorld(String player){
        String select = "SELECT world FROM mailboxes WHERE playername='"+player.toLowerCase()+"'";
        ResultSet r = sqlite.query(select);
        try{
            if(r.next()){
                 String value = r.getString("world");
                 r.close();
                 return value;
            }
            else{
                r.close();
                return "";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public void sqlConnection(){
        sqlite = new SQLite(plugin.getLogger(),"", "Mailboxes", plugin.getDataFolder().getAbsolutePath());
        try{
            sqlite.open();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableCheck(){
        if(sqlite.checkTable("mailboxes")){
            plugin.getLogger().info("Database exists, loading");

            return;
        }
        else{
            plugin.getLogger().info("Database does not exist, creating");
            sqlite.createTable("CREATE TABLE mailboxes(id INTEGER PRIMARY KEY AUTOINCREMENT, playername VARCHAR(20), world VARCHAR(20), x DOUBLE, y DOUBLE, z DOUBLE)");
        }
    }
    
}
