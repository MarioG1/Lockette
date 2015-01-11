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
                if(!sign.getLine(y).equalsIgnoreCase("[Everyone]") && !sign.getLine(y).equalsIgnoreCase(Lockette.altEveryone) && !sign.getLine(y).isEmpty()){
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
                        Lockette.log.log(Level.INFO, "[Lockette] Can't convert {0} !", sign.getLine(y));
                        sign.setLine(y, "");
                        if(y == 1){
                            deny = true;
                            sign.setLine(0, "[?]");
                        }
                    }} catch (Exception e){
                        e.printStackTrace();
                        Lockette.log.log(Level.INFO, "[Lockette] Can't convert {0} !", sign.getLine(y));
                        return false;
                    }
                }
            }
            sign.update();
            if(deny){
                return false;
            }
        } else {
            uuid = UUID.fromString(sign.getLine(1).split(";")[1]);
        }
        
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
    }
    
    public static boolean isMember(Sign sign, Player player){
        int y;
        String name;
        UUID uuid;
        
        for(y = 2; y <= 3; ++y){
            if(!sign.getLine(y).equalsIgnoreCase("[Everyone]") && !sign.getLine(y).equalsIgnoreCase(Lockette.altEveryone) && !sign.getLine(y).isEmpty()){
                name = sign.getLine(y).split(";")[0].trim();
                uuid = UUID.fromString(sign.getLine(y).split(";")[1]);	
                if(uuid.equals(player.getUniqueId())){
                //Check if the Player name has changen and update it
                    if(!name.equals(player.getName())){
                        sign.setLine(y, createPlayerString(player));
                        sign.update();
                    }
                    return true;
                } else {
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
