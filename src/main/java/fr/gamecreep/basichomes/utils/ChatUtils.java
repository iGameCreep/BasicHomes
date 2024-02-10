package fr.gamecreep.basichomes.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ChatUtils
{
    public void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(ChatColor.RED + message);
    }

    public void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(ChatColor.AQUA + message);
    }

    public TextComponent generateCommandComponent(final String command, final boolean underlined, final net.md_5.bungee.api.ChatColor color) {
        final TextComponent component = new TextComponent(command);
        component.setColor(color);
        component.setUnderlined(underlined);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return component;
    }

    public TextComponent generateClipboardComponent(final String text, final String toCopy, final boolean underlined, final net.md_5.bungee.api.ChatColor color) {
        final TextComponent component = new TextComponent(text);
        component.setColor(color);
        component.setUnderlined(underlined);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, toCopy));
        return component;
    }

    public TextComponent generateLinkComponent(final String text, final String url, final boolean underlined, final net.md_5.bungee.api.ChatColor color) {
        final TextComponent component = new TextComponent(text);
        component.setColor(color);
        component.setUnderlined(underlined);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return component;
    }

    public TextComponent textToComponent(final String text, final net.md_5.bungee.api.ChatColor color) {
        final TextComponent component = new TextComponent(text);
        component.setColor(color);
        component.setUnderlined(Boolean.FALSE);
        return component;
    }
}