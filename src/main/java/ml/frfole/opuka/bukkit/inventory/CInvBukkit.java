package ml.frfole.opuka.bukkit.inventory;

import ml.frfole.opuka.bukkit.ItemUtils;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CInvBukkit extends ConfigInventory {
  private final Inventory inv;

  public CInvBukkit(UUID ownerId) {
    super(ownerId);
    this.inv = Bukkit.createInventory(null, 18, invName);
    this.open(ownerId);
  }

  @Override
  protected void createGame(int minesCount, UUID performer) {
    final GameGridInventory ggi = new GGInvBukkit(54, performer, minesCount);
    Opuka.getInstance().methods.setPlayerGGI(performer, ggi);
  }

  @Override
  public void destroy() {
    List<HumanEntity> viewers = new ArrayList<>(this.inv.getViewers());
    viewers.forEach(p -> {
      if (p != null)
        p.closeInventory();
    });
  }

  @Override
  public void open(UUID target) {
    final Player p = Bukkit.getPlayer(target);
    if (p != null && p.isOnline()) p.openInventory(this.inv);
  }

  @Override
  public void update() {
    // info item
    ItemStack item = new ItemStack(Material.SIGN);
    ItemUtils.setName(item, "§rInfo");
    ItemUtils.setLore(item, List.of("§7Bukkit impl by §9frfole", "§7Core by §9frfole", "", "§6https://github.com/frfole/Opuka"));
    inv.setItem(SLOT_INFO, item);
    // play item
    item = new ItemStack(Material.LIME_CONCRETE_POWDER);
    ItemUtils.setName(item, "§2Start the game");
    ItemUtils.setLore(item, List.of("§7Click to start the game", "§7with §9" + minesCount + " §7mine(s)!"));
    inv.setItem(SLOT_PLAY, item);
    // less mines
    item = new ItemStack(Material.REDSTONE,  minesCount - (minesCount <= 1 ? 0 : 1));
    ItemUtils.setName(item, "§cLess mines");
    ItemUtils.setLore(item, List.of("§7Click to remove 1 mine."));
    inv.setItem(SLOT_MINES_LESS, item);
    // mines count
    item = new ItemStack(Material.TNT, minesCount);
    ItemUtils.setName(item, "§rMines: §9" + minesCount);
    ItemUtils.setLore(item, List.of("§7Click to reset mines count to 8", "", "§7min: 1, max: 52"));
    inv.setItem(SLOT_MINES_CURRENT, item);
    // more mines
    item = new ItemStack(Material.LIME_CONCRETE, minesCount + (minesCount >= 52 ? 0 : 1));
    ItemUtils.setName(item, "§2More mines");
    ItemUtils.setLore(item, List.of("§7Click to add 1 mine."));
    inv.setItem(SLOT_MINES_MORE, item);
  }

  @Override
  public Object getInventory() {
    return inv;
  }
}
