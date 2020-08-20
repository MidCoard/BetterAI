package com.focess.betterai;

import com.focess.betterai.api.command.Command;
import com.focess.betterai.command.BetterAICommand;
import com.focess.betterai.listener.EntityListener;
import com.focess.betterai.navigation.ZombieNavigation;
import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.pathfinder.core.navigation.NavigationManager;

import com.toolapi.utils.TestUtil;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class BetterAI extends JavaPlugin {

    private static BetterAI instance;
    public static List<ZombieNavigation> registers=new ArrayList<>();

    public static BetterAI getInstance() {
        return instance;
    }

    public void onEnable(){
        instance = this;
        
        loadConfig();//加载配置文件
        BetterAIConfiguration.loadDefault(this);
        
        this.getServer().getPluginManager().registerEvents(new EntityListener(),this);
        //注册事件
        
        Command.register(new BetterAICommand());
        //注册指令
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
    	for(ZombieNavigation na:registers) {
    		NavigationManager.INSTANCE.unregisterFocessNavigation(na);
    	}
    	Command.unregisterAllCommand();
    }
}
