package fr.gamecreep.basichomes.entities.commands;

import java.util.*;

import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;
import fr.gamecreep.basichomes.entities.accounts.PlayerAccount;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import fr.gamecreep.basichomes.BasicHomes;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

import javax.annotation.Nullable;

public class Account implements CommandExecutor, TabCompleter
{
    private final BasicHomes plugin;
    private final ChatUtils chatUtils = new ChatUtils();

    public Account(final BasicHomes plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        final Player playerSender = (Player) commandSender;
        PlayerAccount acc = this.plugin.getPlayerAccount(playerSender);

        final String accountWebsite = this.plugin.getConfig().getString("accounts.website");
        final TextComponent websiteComponent = this.plugin.getChatUtils().generateLinkComponent(accountWebsite, accountWebsite, true, ChatColor.YELLOW);
        final TextComponent websiteText = this.plugin.getChatUtils().textToComponent("Head over to ", ChatColor.AQUA);
        websiteText.addExtra(websiteComponent);
        websiteText.addExtra(this.plugin.getChatUtils().textToComponent(" to login !", ChatColor.AQUA));

        if (args.length == 0) {
            return this.sendPlayerHelpMessage(playerSender);
        }

        switch (args[0]) {
            case "create": {
                if (acc == null) {
                    acc = this.plugin.createPlayerAccount(playerSender);
                    if (acc == null) {
                        this.chatUtils.sendPlayerError(playerSender, "Could not create your account. Contact a server administrator or check the server logs if you are one");
                        return true;
                    }

                    final TextComponent message = this.plugin.getChatUtils().textToComponent("Successfully created your account ! ", ChatColor.AQUA);
                    message.addExtra(websiteText);
                    final TextComponent loginsMessage = this.plugin.getChatUtils().textToComponent("User ID: ", ChatColor.AQUA);
                    loginsMessage.addExtra(this.getAccIdComponent(acc));
                    loginsMessage.addExtra(this.plugin.getChatUtils().textToComponent(" | Password: ", ChatColor.AQUA));
                    loginsMessage.addExtra(this.getAccPasswordComponent(acc));

                    playerSender.spigot().sendMessage(message);
                    playerSender.spigot().sendMessage(loginsMessage);

                    return true;
                }
                final TextComponent accAlreadyExists = this.plugin.getChatUtils().textToComponent("It seems like you already have an account ! Use ", ChatColor.AQUA);
                accAlreadyExists.addExtra(this.plugin.getChatUtils().generateCommandComponent("/account help", true, ChatColor.YELLOW));
                accAlreadyExists.addExtra(this.plugin.getChatUtils().textToComponent(" to get all of the account-related commands !", ChatColor.AQUA));

                playerSender.spigot().sendMessage(accAlreadyExists);

                return true;
            }
            case "id": {
                if (acc == null) {
                    this.noAccount(playerSender);
                    return true;
                }

                final TextComponent idMsgComponent = this.plugin.getChatUtils().textToComponent("Your account ID is ", ChatColor.AQUA);
                idMsgComponent.addExtra(this.getAccIdComponent(acc));
                idMsgComponent.addExtra(this.plugin.getChatUtils().textToComponent(" !", ChatColor.AQUA));

                playerSender.spigot().sendMessage(idMsgComponent);
                playerSender.spigot().sendMessage(websiteText);

                return true;
            }
            case "reset-password": {
                if (acc == null) {
                    this.noAccount(playerSender);
                    return true;
                }

                final PlayerAccount newAcc = this.plugin.resetAccountPassword(playerSender);

                if (newAcc == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "Could not reset your account password. Contact a server administrator or check the server logs if you are one");
                    return true;
                }

                final TextComponent message2 = this.plugin.getChatUtils().textToComponent("Successfully reset your account ! ", ChatColor.AQUA);
                message2.addExtra(websiteText);
                final TextComponent loginsMessage2 = this.plugin.getChatUtils().textToComponent("User ID: ", ChatColor.AQUA);
                loginsMessage2.addExtra(this.getAccIdComponent(newAcc));
                loginsMessage2.addExtra(this.plugin.getChatUtils().textToComponent(" | Password: ", ChatColor.AQUA));
                loginsMessage2.addExtra(this.getAccPasswordComponent(newAcc));

                playerSender.spigot().sendMessage(message2);
                playerSender.spigot().sendMessage(loginsMessage2);

                return true;
            }
            case "delete": {
                if (acc == null) {
                    this.noAccount(playerSender);
                    return true;
                }

                this.plugin.deletePlayerAccount(playerSender);
                this.plugin.getChatUtils().sendPlayerInfo(playerSender, "Successfully deleted your account !");

                return true;
            }
            default: {
                return this.sendPlayerHelpMessage(playerSender);
            }
        }
    }

    @Nullable
    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String s, @NonNull final String[] strings) {
        if (commandSender instanceof Player) {
            return Arrays.asList("help", "create", "id", "reset-password", "delete");
        }
        return null;
    }

    private void noAccount(final Player player) {
        final TextComponent cmdComponent = this.plugin.getChatUtils().generateCommandComponent("/account create", true, ChatColor.YELLOW);
        final TextComponent endComponent = new TextComponent(" to create an account !");
        final TextComponent component = new TextComponent("You don't have an account ! Use ");
        component.setColor(ChatColor.RED);
        component.addExtra(cmdComponent);
        component.addExtra(endComponent);

        player.spigot().sendMessage(component);
    }

    private TextComponent getAccIdComponent(final PlayerAccount acc) {
        return this.plugin.getChatUtils().generateClipboardComponent(String.valueOf(acc.getUserId()), String.valueOf(acc.getUserId()), true, ChatColor.YELLOW);
    }

    private TextComponent getAccPasswordComponent(final PlayerAccount acc) {
        return this.plugin.getChatUtils().generateClipboardComponent(acc.getPassword(), acc.getPassword(), true, ChatColor.YELLOW);
    }

    private boolean sendPlayerHelpMessage(final Player player) {
        final Map<String, String> cmdList = Map.of(
                "/account create", "Create your account to manage your homes online !",
                "/account id", "Retrieve your account ID !",
                "/account reset-password", "Reset the password of your account !",
                "/account delete", "Delete your account !"
        );

        this.plugin.getChatUtils().sendPlayerInfo(player, "Here are all of the account commands !");

        for (final Map.Entry<String, String> entry : cmdList.entrySet()) {
            final TextComponent component = this.plugin.getChatUtils().generateCommandComponent(entry.getKey(),true, ChatColor.YELLOW);
            component.addExtra(this.plugin.getChatUtils().textToComponent(" | " + entry.getValue(), ChatColor.AQUA));
            player.spigot().sendMessage(component);
        }

        return true;
    }
}