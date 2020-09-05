package com.focess.betterai.utils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public interface ConsoleCommandExecutor extends CommandExecutor {

    @Override
    default CommandResult execute(final CommandSender sender, DataCollection dataCollection) {
        if (sender instanceof ConsoleCommandSender)
            return this.execute((ConsoleCommandSender) sender, dataCollection);
        return CommandResult.REFUSE;
    }

    CommandResult execute(ConsoleCommandSender sender,DataCollection dataCollection);

}
