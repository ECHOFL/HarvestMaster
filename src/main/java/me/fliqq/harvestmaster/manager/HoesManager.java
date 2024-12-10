package me.fliqq.harvestmaster.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.fliqq.harvestmaster.HarvestMaster;
import me.fliqq.harvestmaster.enumerate.ColorLevel;
import me.fliqq.harvestmaster.enumerate.Enchants;
import me.fliqq.harvestmaster.object.PersonalHoe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class HoesManager {
    private final File file;
    private FileConfiguration config;
    private final HarvestMaster plugin;
    private final Map<UUID, PersonalHoe> hoes;

    public HoesManager(HarvestMaster plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "hoes.yml");
        this.hoes = new HashMap<>();
        loadFile();
    }

    private void loadFile() {
        if (!file.exists()) {
            plugin.saveResource("hoes.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public PersonalHoe loadPlayerHoe(Player player) {
        UUID uuid = player.getUniqueId();
        String path = uuid.toString();
        
        // If no data exists for the player, return a new PersonalHoe with default values
        if (!config.contains(path)) {
            PersonalHoe hoe = new PersonalHoe(uuid, 1, 0, getDefaultEnchants());
            hoes.put(uuid, hoe);
            return hoe;
        }

        ConfigurationSection playerSection = config.getConfigurationSection(path + ".hoe");
        Map<Enchants, Integer> enchants = new HashMap<>();
        
        if (playerSection != null) {
            int lvl = playerSection.getInt("level", 1);
            int prestige = playerSection.getInt("prestige", 0);
            ConfigurationSection enchantSection = playerSection.getConfigurationSection("enchants");

            if (enchantSection != null) {
                for (String enchantName : enchantSection.getKeys(false)) {
                    Enchants ench = Enchants.valueOf(enchantName);
                    int enchantLvl = enchantSection.getInt(enchantName, 0);
                    enchants.put(ench, enchantLvl);
                }
            }
            PersonalHoe hoe = new PersonalHoe(uuid, lvl, prestige, enchants);
            hoes.put(uuid, hoe); // Add to the map
            return hoe;
            
        }
        
        // Return a new PersonalHoe with default values in case of unexpected null
        PersonalHoe hoe = new PersonalHoe(uuid, 1, 0, getDefaultEnchants());
        hoes.put(uuid, hoe);
        return hoe;
    }

    public void savePlayerHoe(Player player) {
        UUID uuid = player.getUniqueId();
        String path = uuid.toString() + ".hoe";
        PersonalHoe hoe = hoes.get(uuid);
        
        if (hoe == null) {
            plugin.getLogger().warning("No PersonalHoe found for player " + player.getName() + " when saving.");
            return; // Skip saving if no hoe exists
        }

        ConfigurationSection hoeSection = config.createSection(path);
        hoeSection.set("level", hoe.getLevel());
        hoeSection.set("prestige", hoe.getPrestige());
        
        ConfigurationSection enchantSection = hoeSection.createSection("enchants");
        Map<Enchants, Integer> enchants = hoe.getEnchants();
        
        for (Enchants ench : enchants.keySet()) {
            enchantSection.set(ench.name(), enchants.get(ench));
        }

        // Save the configuration file
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPlayerData(UUID uuid) {
        return hoes.containsKey(uuid) || config.contains(uuid.toString() + ".hoe");
    }

    public void actualisePlayerHoe(Player player) {
        player.getInventory().setItem(0, createHoe(player));
    }

    private ItemStack createHoe(Player player) {
        ItemStack item = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item; // Return default item if meta is null
        }

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        PersonalHoe personalHoe = hoes.get(player.getUniqueId());
        
        if (personalHoe == null) {
            return item; // Return default item if no PersonalHoe found
        }
        
        int level = personalHoe.getLevel();
        int prestige = personalHoe.getPrestige();
        
        // Set color based on level
        TextColor color = ColorLevel.getColor(level);
        
        // Create display name with player's name and hoe level color
        Component displayName = Component.text(player.getName() + "'s ")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD)
                .append(Component.text("Master Hoe").color(color));
        
        // Set display name to meta
        meta.displayName(displayName);
        
        // Create lore list
        List<Component> lore = new ArrayList<>();
        
        // Add level and prestige lines to lore 
        lore.add(Component.text("Level: " + level).color(NamedTextColor.GRAY));
        lore.add(Component.text("Prestige: " + prestige).color(NamedTextColor.GRAY));
        
        // Retrieve enchantments and add them to lore
        Map<Enchants, Integer> enchants = personalHoe.getEnchants();
        
        for (Map.Entry<Enchants, Integer> entry : enchants.entrySet()) {
            Enchants enchant = entry.getKey();
            int enchantLevel = entry.getValue();

            // Only add to lore if enchantment level is greater than 0
            if (enchantLevel > 0) {
                Component enchantLore = Component.text(enchant.getName() + " Level: " + enchantLevel)
                        .color(ColorLevel.getColor(enchantLevel));
                lore.add(enchantLore);
            }
        }
        
        // Set the complete lore to the item's meta
        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private Map<Enchants, Integer> getDefaultEnchants() {
       Map<Enchants, Integer> defaultEnchants = new HashMap<>();
       
       // Initialize all possible enchantments to level 0
       for (Enchants enchant : Enchants.values()) {
           defaultEnchants.put(enchant, 0); // Set to 0 or any default value you prefer
       }
       
       return defaultEnchants;
   }

   public Map<UUID, PersonalHoe> getHoes() {
       return hoes;
   }
}
