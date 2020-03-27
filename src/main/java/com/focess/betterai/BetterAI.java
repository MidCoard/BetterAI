package com.focess.betterai;

import com.focess.betterai.command.BetterAICommand;
import com.focess.betterai.listener.EntityListener;
import com.focess.betterai.util.BetterAIConfiguration;
import com.focess.betterai.util.command.Command;
import org.bukkit.entity.Animals;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.zip.ZipFile;

public class BetterAI extends JavaPlugin {

    private static BetterAI instance;

    public static BetterAI getInstance() {
        return instance;
    }

    public void onEnable(){
        instance = this;
        loadConfig();
        BetterAIConfiguration.loadDefault(this);
        this.getServer().getPluginManager().registerEvents(new EntityListener(),this);
        Command.register(new BetterAICommand());
    }

    private void loadConfig() {
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists())
            this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }
}
