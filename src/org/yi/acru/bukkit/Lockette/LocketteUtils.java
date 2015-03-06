/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yi.acru.bukkit.Lockette;

import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario
 */
public class LocketteUtils {
    
    public static boolean isOwner(Sign sign, Player player){
        UUID uuid = null, tmp_uuid;
        OfflinePlayer oplayer;
        boolean deny = false;
        
        String line = sign.getLine(1);
        String name = line.split(";")[0].trim();
        
        if(line.split(";").length == 1)
        {
            Lockette.log.log(Level.INFO, "[Lockette] Old sign found converting to new format");
            for(int y = 1; y <= 3; ++y){
                if(!sign.getLine(y).equalsIgnoreCase("[Everyone]") && !sign.getLine(y).equalsIgnoreCase(Lockette.altEveryone) && !sign.getLine(y).isEmpty() && sign.getLine(y).split(";").length == 1){
                    oplayer = Bukkit.getOfflinePlayer(ChatColor.stripColor(sign.getLine(y)));
                    Lockette.log.log(Level.INFO, "[Lockette] Converting {0} !", oplayer.getName());
                    try{
                    if(oplayer.hasPlayedBefore()){
                        tmp_uuid = oplayer.getUniqueId();
                        sign.setLine(y,createPlayerString(oplayer));
                        if(y == 1){
                            uuid = tmp_uuid;
                        }                                   
                    } else {
                        Lockette.log.log(Level.INFO, "[Lockette] Can not convert {0} !", oplayer.getName());
                        sign.setLine(y, "");
                        if(y == 1){
                            deny = true;
                            sign.setLine(0, "[?]");
                        }
                    }} catch (Exception e){
                        Lockette.log.log(Level.INFO, "[Lockette] Can't convert {0} !", sign.getLine(y));
                        sign.setLine(y, "");
                        if(y == 1){
                            deny = true;
                            sign.setLine(0, "[?]");
                        }
                    }
                }
            }
            sign.update();
            if(deny){
                return false;
            }
        } else {
            if(!sign.getLine(1).isEmpty()){
                uuid = UUID.fromString(sign.getLine(1).split(";")[1]);
            } else {
                return false;
            }
        }
        
        if(sign.getLine(0).equalsIgnoreCase("[Private]") || sign.getLine(0).equalsIgnoreCase(Lockette.altPrivate)){
            if(uuid.equals(player.getUniqueId())){
                //Check if the Player name has changen and update it
                if(!name.equals(player.getName())){
                    sign.setLine(1, createPlayerString(player));
                    sign.update();
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static boolean isMember(Sign sign, Player player){
        int y;
        String name;
        UUID uuid;
       
        for(y = 1; y <= 3; ++y){
            if(!sign.getLine(y).equalsIgnoreCase("[Everyone]") && !sign.getLine(y).equalsIgnoreCase(Lockette.altEveryone) && !sign.getLine(y).isEmpty()){
                try {
                    name = sign.getLine(y).split(";")[0].trim();
                    uuid = UUID.fromString(sign.getLine(y).split(";")[1]);
                    if(uuid.equals(player.getUniqueId())){
                    //Check if the Player name has changen and update it
                        if(!name.equals(player.getName())){
                            sign.setLine(y, createPlayerString(player));
                            sign.update();
                        }
                        return true;
                    } 
                } catch (ArrayIndexOutOfBoundsException e){
                    Lockette.log.log(Level.WARNING, "[Lockette] Strange singn found. Line: " + sign.getLine(y));
                    return false;
                }
            }		
        } return false;
    }
    
    
    public static boolean hasAccess(Sign sign, Player player){
        if(isOwner(sign,player)) return true;
        if(isMember(sign, player)) return true;
        
        //Check for [everyone]
        for(int y = 1; y <= 3; ++y){
            if(sign.getLine(y).equalsIgnoreCase("[Everyone]") || sign.getLine(y).equalsIgnoreCase(Lockette.altEveryone)){
                return true;
            }
        }
        return false;
    }
    
    public static String createPlayerString(String player){
        if(!player.isEmpty())
        {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(player);
            if(oplayer.hasPlayedBefore()){
                return createPlayerString(oplayer);
            } else {
                return "";
            }                
        } else {
            return "";
        }
    }
    
    public static String createPlayerString(OfflinePlayer player){
        return player.getName() + "                         ;" + player.getUniqueId().toString();
    }
    
}
