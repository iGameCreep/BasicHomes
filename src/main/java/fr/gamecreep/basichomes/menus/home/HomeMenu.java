package fr.gamecreep.basichomes.menus.home;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
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

public class HomeMenu extends PaginatedMenu {

    private final BasicHomes plugin;
    private final Player target;

    public HomeMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player,
                    @NonNull final Player target) {
        super(
                player,
                target,
                Constants.MY_HOMES_MENU_NAME,
                Permission.USE_HOME
        );
        this.plugin = plugin;
        this.target = target;
    }

    public HomeMenu(@NonNull final BasicHomes plugin,
                    @NonNull final Player player) {
        super(
                player,
                player,
                Constants.MY_HOMES_MENU_NAME,
                Permission.USE_HOME
        );
        this.plugin = plugin;
        this.target = player;
    }

    @Override
    protected void onMenuClickEvent(@NonNull InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        SavedPosition home = getPosByPersistentData(dataContainer);
        if (home == null) return;

        if (item.getType().equals(Constants.DELETE_ITEM)) {
            this.plugin.getHomeHandler().delete(home);
            super.getPlayer().closeInventory();
        } else {
            //TODO: Teleport player
            super.getPlayer().closeInventory();
            //TODO: Send player message to confirm home deletion
        }

    }

    @Nullable
    private SavedPosition getPosByPersistentData(PersistentDataContainer dataContainer) {
        String savedHomeId = dataContainer.get(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING);
        if (savedHomeId == null) return null;
        UUID homeId = UUID.fromString(savedHomeId);
        return this.plugin.getHomeHandler().getById(this.target, homeId);
    }
}
