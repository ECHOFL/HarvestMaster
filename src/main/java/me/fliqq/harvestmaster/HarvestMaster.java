package me.fliqq.harvestmaster;

import org.bukkit.plugin.java.JavaPlugin;

import me.fliqq.harvestmaster.command.ActualiseHoeCommand;
import me.fliqq.harvestmaster.listener.JoinListener;
import me.fliqq.harvestmaster.manager.HoesManager;

public class HarvestMaster extends JavaPlugin
{
    private HoesManager hoesManager;
    @Override
    public void onEnable(){
        hoesManager= new HoesManager(this);

        getServer().getPluginManager().registerEvents(new JoinListener(hoesManager),this);
        messages();
        getCommand("hoe").setExecutor(new ActualiseHoeCommand(hoesManager));
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("HarvestMaster 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
