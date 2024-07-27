package me.cire3.stuffdumperidk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class StuffDumperIdk extends JavaPlugin implements Listener {
    private FileOutputStream fos;
    private PrintWriter pw;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            pw.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getPluginManager().registerEvents(this, this);
        try {
            fos = new FileOutputStream(new File(getDataFolder(), "stuffdumper.dat"));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (fos == null || pw == null)
            return;

        pw.println("Player: " + e.getPlayer().getName());
        pw.println("Interact Type: " + e.getAction());
        pw.println("Has Item: " + e.hasItem());
        if (e.hasItem()) {
            ItemStack i = e.getItem();
            pw.println("Interact Item: " + i.serialize());
            pw.println("Is Mystery Book Strict: " + (i != null && i.getType() == Material.BOOK && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals(ChatColor.GRAY + "Examine to receive a random")));
            pw.println("Is Mystery Book Soft1: " + (i != null && i.getType() == Material.BOOK && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equalsIgnoreCase(ChatColor.GRAY + "Examine to receive a random")));
            pw.println("Is Mystery Book Soft2: " + (i != null && i.getType() == Material.BOOK && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).contains("Examine to receive a random")));
        }
    }
}
