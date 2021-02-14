package ml.frfole.opuka.minestom;

import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import ml.frfole.opuka.minestom.commands.OpukaCommand;
import ml.frfole.opuka.minestom.inventory.CInvMinestom;
import ml.frfole.opuka.minestom.inventory.GGInvMinestom;
import ml.frfole.opuka.minestom.listeners.InventoryListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class OpukaMinestom extends Extension {
  private static OpukaMinestom instance;
  protected Task updateTask;
  protected OpukaCommand opukaCommand = new OpukaCommand();
  protected File dataFolder;


  @Override
  public void initialize() {
    instance = this;
    dataFolder = new File(MinecraftServer.getExtensionManager().getExtensionFolder(), "Opuka");
    getResource("lang.json");
    new Opuka(new MinestomMethods(System.currentTimeMillis()), dataFolder);
    MinecraftServer.getCommandManager().register(opukaCommand);
    updateTask = MinecraftServer.getSchedulerManager().buildTask(Opuka.getInstance().methods::tick).repeat(250, TimeUnit.MILLISECOND).schedule();
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    globalEventHandler.addEventCallback(InventoryCloseEvent.class, InventoryListener::onClose);
    globalEventHandler.addEventCallback(InventoryPreClickEvent.class, InventoryListener::onClick);
  }

  @Override
  public void terminate() {
    updateTask.cancel();
    MinecraftServer.getCommandManager().unregister(opukaCommand);
    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    globalEventHandler.removeEventCallback(InventoryCloseEvent.class, InventoryListener::onClose);
    globalEventHandler.removeEventCallback(InventoryPreClickEvent.class, InventoryListener::onClick);
  }

  public InputStream getResource(String fileName) {
    final Path targetFile = dataFolder.toPath().resolve(fileName);
    try {
      // Copy from jar if the file does not exist in extension directory
      if (!Files.exists(targetFile)) {
        savePackagedResource(fileName);
      }

      return Files.newInputStream(targetFile);
    } catch (IOException ex) {
      getLogger().debug("Failed to read resource {}.", fileName, ex);
      return null;
    }
  }

  /**
   * Gets a resource from inside the extension jar.
   *
   * @param fileName The file to read
   * @return The file contents, or null if there was an issue reading the file.
   */
  public InputStream getPackagedResource(String fileName) {
    try {
      final URL url = getClass().getClassLoader().getResource(fileName);
      if (url == null) {
        getLogger().debug("Resource not found: {}", fileName);
        return null;
      }

      return url.openConnection().getInputStream();
    } catch (IOException ex) {
      getLogger().debug("Failed to load resource {}.", fileName, ex);
      return null;
    }
  }

  /**
   * Copies a resource file to the extension directory, replacing any existing copy.
   *
   * @param fileName The resource to save
   * @return True if the resource was saved successfully, null otherwise
   */
  public boolean savePackagedResource(String fileName) {
    final Path targetFile = dataFolder.toPath().resolve(fileName);
    try (InputStream is = getPackagedResource(fileName)) {
      Files.createDirectories(targetFile.getParent());

      if (is == null) {
        return false;
      }

      Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
      return true;
    } catch (IOException ex) {
      getLogger().info("Failed to save resource {}.", fileName, ex);
      return false;
    }
  }

  public static OpukaMinestom getInstance() {
    return instance;
  }

  private static class MinestomMethods extends Opuka.Methods {
    public MinestomMethods(long seed) {
      this.random = new Random(seed);
    }

    @Override
    public ConfigInventory createCI(UUID owner) {
      return new CInvMinestom(owner);
    }

    @Override
    public GameGridInventory createGGI(UUID owner, int minesCount) {
      return new GGInvMinestom(owner, minesCount);
    }

    @Override
    public void tick() {
      super.tick();
      OpukaMinestom.instance.opukaCommand.update();
    }
  }
}
