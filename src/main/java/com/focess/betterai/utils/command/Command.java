package com.focess.betterai.utils.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Command extends org.bukkit.command.Command {

    //todo need to be proven that it is valid.
    public static final Permission DEFAULT_PERMISSION = new Permission("");

    private static final List<Command> commands = Lists.newArrayList();
    private static CommandMap commandMap;

    static {
        try {
            Command.getCommandMap();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private final List<Executor> executors = Lists.newArrayList();
    private boolean registered;

    public Command(final String name, final List<String> ali) {
        super(name, "", "", ali);
        this.init();
        Command.commandMap.register("FocessCommand", this);
    }

    private static void getCommandMap() throws Exception {
        final Class<?> c = Bukkit.getServer().getClass();
        Command.commandMap = (CommandMap) c.getDeclaredMethod("getCommandMap", new Class[0]).invoke(Bukkit.getServer(),
                new Object[0]);
    }

    public static void register(final Command command) {
        command.registered = true;
        Command.commands.add(command);
    }

    public static void unregisterAllCommand() {
        for (Command command : commands)
            command.unregister();
    }

    public final Executor addExecutor(final int count, final CommandExecutor executor, final String... subCommands) {
        Executor executor1 = new Executor(count, subCommands).addExecutor(executor);
        this.executors.add(executor1);
        return executor1;
    }

    @Override
    public final boolean execute(final CommandSender sender, final String cmd, final String[] args) {
        if (!this.registered)
            return true;
        final int amount = args.length;
        boolean flag = false;
        for (final Executor executor : this.executors)
            if (executor.checkCount(amount) && executor.checkArgs(args)) {
                CommandResult result;
                if (sender.hasPermission(executor.permission))
                    result = executor.execute(sender, Arrays.copyOfRange(args, executor.getSubCommandsSize(), args.length));
                else result = CommandResult.REFUSE;
                for (CommandResult r : executor.results.keySet())
                    if ((r.getPos() & result.getPos()) == 1)
                        executor.results.get(r).execute();
                flag = true;
                break;
            }
        if (!flag)
            this.usage(sender);
        return true;
    }

    public final void unregister() {
        try {
            final Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            final Map<String, org.bukkit.command.Command> commands = (Map<String, org.bukkit.command.Command>) field.get(commandMap);
            commands.remove("focesscommand:" + this.getName().toLowerCase());
            for (final String alias : this.getAliases())
                commands.remove("focesscommand:" + alias.toLowerCase());
            field.set(commandMap, commands);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.unregister(Command.commandMap);
        this.registered = false;
    }

    protected List<String> getCompleteLists(CommandSender sender, String cmd, String[] args) {
        return Lists.newArrayList();
    }

    public abstract void init();

    @Override
    public final List<String> tabComplete(final CommandSender sender, final String cmd, final String[] args) {
        final List<String> ret = this.getCompleteLists(sender, cmd, args);
        if (args == null || args.length == 0) {
            return ret;
        }
        if (ret == null || ret.size() == 0) {
            for (final Executor executor : this.executors)
                if (args.length - 1 >= executor.getSubCommandsSize() && executor.checkArgs(args)) {
                    int pos = args.length - executor.getSubCommandsSize();
                    if (executor.tabCompletes.length < pos)
                        continue;
                    boolean flag = false;
                    for (int i = 0;i < pos - 1;i++)
                        if (!executor.tabCompletes[i].accept(args[i + executor.getSubCommandsSize()])) {
                            flag = true;
                            break;
                        }
                    if (!flag)
                        ret.addAll(executor.tabCompletes[pos - 1].getTabComplete(sender));
                }
            for (Executor executor:this.executors)
                if (args.length <= executor.getSubCommandsSize() && executor.checkArgs(args,executor.getSubCommandsSize() - 1))
                    ret.add(executor.subCommands[executor.getSubCommandsSize() - 1]);
        }
        return ret.parallelStream().filter(str -> str.startsWith(args[args.length - 1]))
                    .collect(Collectors.toList());
    }

    public abstract void usage(CommandSender commandSender);

    public static class Executor {
        private final int count;
        private final String[] subCommands;
        private CommandExecutor executor;
        private Permission permission = Command.DEFAULT_PERMISSION;
        private TabCompleter[] tabCompletes = new TabCompleter[0];
        private Map<CommandResult,CommandResultExecutor> results = Maps.newHashMap();
        private DataConverter[] dataConverters;
        private boolean useDefaultConverter = true;

        private Executor(final int count, final String... subCommands) {
            this.subCommands = subCommands;
            this.count = count;
        }

        private Executor addExecutor(final CommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        private boolean checkArgs(final String[] args) {
            return this.checkArgs(args,this.getSubCommandsSize());
        }

        private boolean checkArgs(final String[] args,int count) {
            for (int i = 0; i < count; i++)
                if (!this.subCommands[i].equals(args[i]))
                    return false;
            return true;
        }

        private boolean checkCount(final int amount) {
            return this.subCommands.length + this.count == amount;
        }

        private CommandResult execute(final CommandSender sender, final String[] args) {
            if (this.useDefaultConverter) {
                List<DataConverter<?>> dataConverters = Lists.newArrayList();
                for (TabCompleter tabCompleter:this.tabCompletes)
                    dataConverters.add(tabCompleter);
                for (int i = 0;i<args.length - dataConverters.size();i++)
                    dataConverters.add(DataConverter.DEFAULT_DATA_CONVERTER);
                this.dataConverters = dataConverters.toArray(new DataConverter[0]);
            }
            else if (this.dataConverters.length < args.length) {
                List<DataConverter<?>> dataConverters = Lists.newArrayList(this.dataConverters);
                for (int i = 0;i<args.length - this.dataConverters.length;i++)
                    dataConverters.add(DataConverter.DEFAULT_DATA_CONVERTER);
                this.dataConverters = dataConverters.toArray(new DataConverter[0]);
            }
            DataCollection dataCollection = new DataCollection(args.length);
            for (int i = 0;i<args.length;i++)
                if (!this.dataConverters[i].put(dataCollection,args[i]))
                    return CommandResult.ARGS;
                dataCollection.flip();
            return this.executor.execute(sender, dataCollection);
        }

        private int getSubCommandsSize() {
            return this.subCommands.length;
        }


        public Executor addPermission(Permission permission) {
            this.permission = permission;
            return this;
        }

        public Executor addTabComplete(TabCompleter... tabCompleters) {
            this.tabCompletes = tabCompleters;
            return this;
        }

        public Executor addCommandResult(CommandResult result, CommandResultExecutor executor) {
            results.put(result,executor);
            return this;
        }

        public Executor addDataConverter(DataConverter...dataConverters) {
            this.dataConverters = dataConverters;
            this.useDefaultConverter = false;
            return this;
        }

        public Executor setUseDefaultConverter(boolean flag) {
            this.useDefaultConverter = flag;
            return this;
        }
    }
}
