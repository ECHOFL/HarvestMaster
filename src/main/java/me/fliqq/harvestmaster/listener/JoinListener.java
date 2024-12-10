package me.fliqq.harvestmaster.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fliqq.harvestmaster.manager.HoesManager;

public class JoinListener implements Listener{

    private final HoesManager hoesManager;
    public JoinListener(HoesManager hoesManager){
        this.hoesManager=hoesManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        hoesManager.loadPlayerHoe(player);
        hoesManager.actualisePlayerHoe(player);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        hoesManager.savePlayerHoe(player);
    }
}
