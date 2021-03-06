package ml.frfole.opuka.minestom.inventory;

import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.minestom.ItemUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CInvMinestom extends ConfigInventory {
  private final Inventory inv;

  public CInvMinestom(UUID ownerId) {
    super(ownerId);
    this.inv = new Inventory(InventoryType.CHEST_2_ROW, this.invName);
    this.open(ownerId);
  }

  @Override
  public void destroy() {
    final Set<Player> players = inv.getViewers();
    players.forEach(player -> {
      if (player != null)
        player.closeInventory();
    });
  }

  @Override
  public void open(UUID target) {
    final Player player = MinecraftServer.getConnectionManager().getPlayer(target);
    if (player != null && player.isOnline()) {
      player.openInventory(this.inv);
    }
  }

  @Override
  public void update() {
    final Map<String, String> placeholders = new HashMap<>();
    placeholders.put("mines", String.valueOf(minesCount));
    placeholders.put("implname", "Minestom");
    placeholders.put("implauthor", "frfole");

    ItemStack item = new ItemStack(Material.OAK_SIGN, (byte)1);
    ItemUtils.setName(item, "opuka.inventory.config.item.info.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.config.item.info.lore", placeholders);
    inv.setItemStack(SLOT_INFO, item);

    item = new ItemStack(Material.LIME_CONCRETE_POWDER, (byte)1);
    ItemUtils.setName(item, "opuka.inventory.config.item.play.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.config.item.play.lore", placeholders);
    inv.setItemStack(SLOT_PLAY, item);

    item = new ItemStack(Material.REDSTONE, (byte) (minesCount - (minesCount <= 1 ? 0 : 1)));
    ItemUtils.setName(item, "opuka.inventory.config.item.less_mines.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.config.item.less_mines.lore", placeholders);
    inv.setItemStack(SLOT_MINES_LESS, item);

    item = new ItemStack(Material.TNT, (byte) minesCount);
    ItemUtils.setName(item, "opuka.inventory.config.item.mines.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.config.item.mines.lore", placeholders);
    inv.setItemStack(SLOT_MINES_CURRENT, item);

    item = new ItemStack(Material.LIME_CONCRETE, (byte) (minesCount + (minesCount >= 52 ? 0 : 1)));
    ItemUtils.setName(item, "opuka.inventory.config.item.more_mines.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.config.item.more_mines.lore", placeholders);
    inv.setItemStack(SLOT_MINES_MORE, item);
  }

  @Override
  public Object getInventory() {
    return this.inv;
  }
}
