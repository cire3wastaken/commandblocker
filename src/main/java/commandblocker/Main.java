package commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {
    private File configFile;
    private FileConfiguration configuration;
    private Set<String> blockedCmds;

    private boolean enabled;
    private final boolean debug = false;

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        enabled = true;
        Bukkit.getPluginManager().registerEvents(this, this);
        defineConfig();
        loadConfig();
        super.onEnable();
    }

    @EventHandler
    public void handleCommand(PlayerCommandPreprocessEvent event){
        if (!enabled) return;

        for (String blocked : blockedCmds){
            String cmd = blocked.startsWith("/") ? blocked : '/' + blocked;

            if (event.getMessage().startsWith(cmd)){
                if (debug)
                    event.getPlayer().sendMessage("Cancelled Command");
                event.setCancelled(true);
            }
        }
    }

    private void defineConfig(){
        this.configFile = new File(getDataFolder(), "config.yml");
        if(!this.configFile.exists()){
            this.configuration = getConfig();
            saveDefaultConfig();
        } else {
            this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
        }
    }

    private void loadConfig(){
        if (configFile == null || configuration == null)
            defineConfig();
        List<String> temp;

        temp = configuration.getStringList("commands-to-block");
        if (temp == null || temp.isEmpty())
            blockedCmds = new HashSet<>();
        else
            blockedCmds = new HashSet<>(temp);
    }
}
