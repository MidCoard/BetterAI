package com.focess.betterai.api.command;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

    void execute(CommandSender sender, String[] args);

}
