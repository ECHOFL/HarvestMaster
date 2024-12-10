package me.fliqq.harvestmaster.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import me.fliqq.harvestmaster.manager.HoesManager;
@AllArgsConstructor
public class ActualiseHoeCommand implements CommandExecutor {

    private final HoesManager hoesManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
                hoesManager.actualisePlayerHoe((Player) sender);
                return true;
    }
    
}
