package com.focess.betterai.utils.command;

import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandExecutor {
    CommandResult execute(CommandSender sender, DataCollection dataCollection);

}
