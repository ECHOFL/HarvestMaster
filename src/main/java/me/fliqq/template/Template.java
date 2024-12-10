package me.fliqq.template;

import org.bukkit.plugin.java.JavaPlugin;

public class Template extends JavaPlugin
{
    @Override
    public void onEnable(){
        messages();
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("TEMPLATE 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
