package fr.gamecreep.basichomes.menus.home;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.menus.tools.PaginatedMenu;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.UUID;

public class HomeMenu extends PaginatedMenu {

    private final BasicHomes plugin;
    private final Player target;
    private final PositionType type;

    public HomeMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player,
                    @NonNull final Player target,
                    @NonNull final PositionType type) {
        super(
                plugin,
                player,
                target,
                Constants.HOMES_OF_START_MENU_NAME + target.getDisplayName(),
                Permission.USE_HOME
        );
        this.plugin = plugin;
        this.target = target;
        this.type = type;
    }

    public HomeMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player,
                    @NonNull final PositionType type) {
        super(
                plugin,
                player,
                player,
                Constants.MY_HOMES_MENU_NAME,
                Permission.USE_HOME
        );
        this.plugin = plugin;
        this.target = player;
        this.type = type;
    }

    @Override
    protected void onMenuClickEvent(@NonNull final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (item == null) return;

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        final PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        final SavedPosition home = getPosByPersistentData(dataContainer);
        if (home == null) return;

        if (item.getType().equals(Constants.DELETE_ITEM)) {
            this.plugin.getPositionDataHandler().delete(home);
            super.getPlayer().closeInventory();
            ChatUtils.sendPlayerInfo(super.getPlayer(), String.format(
                    "The home %s%s%s has been removed !",
                    Constants.SPECIAL_COLOR,
                    home.getName(),
                    Constants.SUCCESS_COLOR
            ));
        } else {
            this.plugin.getTeleportUtils().add(super.getPlayer(), home);
            super.getPlayer().closeInventory();
        }

    }

    @Nullable
    private SavedPosition getPosByPersistentData(final PersistentDataContainer dataContainer) {
        final String savedHomeId = dataContainer.get(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING);
        if (savedHomeId == null) return null;

        final UUID homeId = UUID.fromString(savedHomeId);
        return this.plugin.getPositionDataHandler().getById(this.type, this.target, homeId);
    }
}
