package com.focess.betterai.utils.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.omg.CORBA.DATA_CONVERSION;

public interface PlayerCommandExecutor extends CommandExecutor {

    @Override
    default CommandResult execute(final CommandSender sender, DataCollection dataCollection) {
        if (sender instanceof Player && sender.isOp())
            return this.execute((Player) sender, dataCollection);
        return CommandResult.REFUSE;
    }

    CommandResult execute(Player player, DataCollection dataCollection);
}
