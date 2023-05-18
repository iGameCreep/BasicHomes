package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.accounts.PlayerAccount;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Account implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;

    public Account(BasicHomes plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;
            PlayerAccount acc = plugin.getPlayerAccount(playerSender);
            String accountWebsite = plugin.getConfig().getString("accounts.website");

            if (args.length == 0) { // /account
                if (acc == null) {
                    acc = plugin.createPlayerAccount(playerSender);
                    plugin.addServerToPlayer(playerSender);
                    plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
                            "Successfully created your account ! Head over to§e %s §bto connect !" +
                                    " Username:§e %s §bPassword:§e %s", accountWebsite,  acc.getUserId(), acc.getPassword()));
                    return true;
                }

                plugin.getChatUtils().sendPlayerInfo(playerSender, "It seems like you already have an account !" +
                        "Use §e/account help §bto get all of the account-related commands !");
                return true;
            }

            switch (args[0]) {
                case "id": // /account id
                    if (acc == null) {
                        noAccount(playerSender);
                        return true;
                    }
                    int accountId = acc.getUserId();

                    plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Your account ID is §e%s§b ! Head over to §e%s§b to login !", accountId, accountWebsite));
                    return true;

                case "reset-password":// /account reset-password
                    if (acc == null) {
                        noAccount(playerSender);
                        return true;
                    }
                    PlayerAccount newAcc = plugin.resetAccountPassword(playerSender);
                    plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
                            "Successfully reset your account's password ! Head over to§e %s §bto connect !" +
                                    " Username:§e %s §bPassword:§e %s", accountWebsite, newAcc.getUserId(), newAcc.getPassword()));
                    return true;

                case "add-server": // /account add-server
                    if (acc == null) {
                        noAccount(playerSender);
                        return true;
                    }
                    if (plugin.serverAlreadyRegistered(playerSender)) {
                        plugin.getChatUtils().sendPlayerError(playerSender, "This server is already registered !");
                        return true;
                    }
                    plugin.addServerToPlayer(playerSender);
                    plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Successfully added this server to your account ! Head over to §e%s§b to manage it !", accountWebsite));
                    return true;

                case "delete": // /account delete
                    if (acc == null) {
                        noAccount(playerSender);
                        return true;
                    }
                    plugin.deletePlayerAccount(playerSender);
                    plugin.getChatUtils().sendPlayerInfo(playerSender, "Successfully deleted your account !");
                    return true;

                default: // /account help & misspelled
                    Hashtable<String, String> cmdList = new Hashtable();
                    cmdList.put("/account", "Create your account to manage your homes online !");
                    cmdList.put("/account id", "Retrieve your account ID !");
                    cmdList.put("/account add-server", "Add a new Minecraft server using BasicHomes to your account !");
                    cmdList.put("/account reset-password", "Reset the password of your account !");
                    cmdList.put("/account delete", "Delete your account !");

                    plugin.getChatUtils().sendPlayerInfo(playerSender, "Here are all of the account commands !");
                    for (Map.Entry<String, String> entry : cmdList.entrySet()) {
                        plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Command: §e%s§b | Description: §e%s§b", entry.getKey(), entry.getValue()));
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            List<String> optionsList = new ArrayList<>();
            optionsList.add("id");
            optionsList.add("reset-password");
            optionsList.add("add-server");
            optionsList.add("delete");

            return optionsList;
        }
        return null;
    }

    private void noAccount(Player player) {
        plugin.getChatUtils().sendPlayerInfo(player, "You don't have an account ! Use §e/account §bto create an account !");
    }
}
