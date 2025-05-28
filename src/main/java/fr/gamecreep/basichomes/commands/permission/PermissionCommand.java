package fr.gamecreep.basichomes.commands.permission;

import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class PermissionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        return List.of();
    }
}
