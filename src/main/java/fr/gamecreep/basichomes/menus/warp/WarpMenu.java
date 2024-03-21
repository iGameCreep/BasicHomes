package fr.gamecreep.basichomes.menus.warp;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.menus.tools.PaginatedMenu;
import lombok.NonNull;
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

    public WarpMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player) {
        super(
                player,
                player,
                Constants.WARPS_MENU_NAME,
                Permission.DELETE_WARP
        );
        this.plugin = plugin;
    }

    @Override
    protected void onMenuClickEvent(@NonNull InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        SavedPosition warp = getPosByPersistentData(dataContainer);
        if (warp == null) return;

        if (item.getType().equals(Constants.DELETE_ITEM)) {
            this.plugin.getWarpHandler().delete(warp);
            super.getPlayer().closeInventory();
            this.plugin.getChatUtils().sendPlayerInfo(super.getPlayer(), String.format(
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
    private SavedPosition getPosByPersistentData(PersistentDataContainer dataContainer) {
        String savedWarpId = dataContainer.get(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING);
        if (savedWarpId == null) return null;
        UUID warpId = UUID.fromString(savedWarpId);
        return this.plugin.getWarpHandler().getById(warpId);
    }
}
