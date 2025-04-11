package fr.gamecreep.basichomes.menus.warp;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.menus.tools.PaginatedMenu;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.UUID;

public class WarpMenu extends PaginatedMenu {

    private final BasicHomes plugin;

    private final NamespacedKey paginatedMenuItemsNamespacedKey;

    public WarpMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player) {
        super(
                plugin,
                player,
                player,
                Constants.WARPS_MENU_NAME,
                Permission.DELETE_WARP
        );
        this.plugin = plugin;
        this.paginatedMenuItemsNamespacedKey = new NamespacedKey(plugin, Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS);
    }

    @Override
    protected void onMenuClickEvent(@NonNull final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (item == null) return;

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        final PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        final SavedPosition warp = getPosByPersistentData(dataContainer);
        if (warp == null) return;

        if (item.getType().equals(Constants.DELETE_ITEM)) {
            this.plugin.getPositionDataHandler().delete(warp);
            super.getPlayer().closeInventory();
            ChatUtils.sendPlayerInfo(super.getPlayer(), String.format(
                    "The warp %s%s%s has been removed !",
                    Constants.SPECIAL_COLOR,
                    warp.getName(),
                    Constants.SUCCESS_COLOR
            ));
        } else {
            this.plugin.getTeleportUtils().add(super.getPlayer(), warp);
            super.getPlayer().closeInventory();
        }

    }

    @Nullable
    private SavedPosition getPosByPersistentData(final PersistentDataContainer dataContainer) {
        final String savedWarpId = dataContainer.get(this.paginatedMenuItemsNamespacedKey, PersistentDataType.STRING);
        if (savedWarpId == null) return null;

        final UUID warpId = UUID.fromString(savedWarpId);
        return this.plugin.getPositionDataHandler().getById(PositionType.WARP, warpId);
    }
}
