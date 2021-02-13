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
import java.util.Arrays;
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
    ItemStack item = new ItemStack(Material.PAPER);
    ItemUtils.setName(item, Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.info.name")
            .replaceAll("%mines%", String.valueOf(minesCount)));
    ItemUtils.setLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.info.lore")
            .replaceAll("%mines%", String.valueOf(minesCount))
            .split("\n")));
    inv.setItem(SLOT_INFO, item);
    // play item
    item = new ItemStack(Material.LIME_CONCRETE_POWDER);
    ItemUtils.setName(item, Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.play.name")
            .replaceAll("%mines%", String.valueOf(minesCount)));
    ItemUtils.setLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.play.lore")
                    .replaceAll("%mines%", String.valueOf(minesCount))
                    .split("\n")));
    inv.setItem(SLOT_PLAY, item);
    // less mines
    item = new ItemStack(Material.REDSTONE,  minesCount - (minesCount <= 1 ? 0 : 1));
    ItemUtils.setName(item, Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.less_mines.name")
            .replaceAll("%mines%", String.valueOf(minesCount)));
    ItemUtils.setLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.less_mines.lore")
            .replaceAll("%mines%", String.valueOf(minesCount))
            .split("\n")));
    inv.setItem(SLOT_MINES_LESS, item);
    // mines count
    item = new ItemStack(Material.TNT, minesCount);
    ItemUtils.setName(item, Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.mines.name")
            .replaceAll("%mines%", String.valueOf(minesCount)));
    ItemUtils.setLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.mines.lore")
            .replaceAll("%mines%", String.valueOf(minesCount))
            .split("\n")));
    inv.setItem(SLOT_MINES_CURRENT, item);
    // more mines
    item = new ItemStack(Material.LIME_CONCRETE, minesCount + (minesCount >= 52 ? 0 : 1));
    ItemUtils.setName(item, Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.more_mines.name")
            .replaceAll("%mines%", String.valueOf(minesCount)));
    ItemUtils.setLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get("opuka.inventory.config.item.more_mines.lore")
            .replaceAll("%mines%", String.valueOf(minesCount))
            .split("\n")));
    inv.setItem(SLOT_MINES_MORE, item);
  }

  @Override
  public Object getInventory() {
    return inv;
  }
}
