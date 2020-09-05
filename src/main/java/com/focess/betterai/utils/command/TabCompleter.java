package com.focess.betterai.utils.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class TabCompleter<T> extends DataConverter<T> {


    public static final TabCompleter<Integer> CoordinateIntX = new TabCompleter<Integer>() {
        List<String> getTabComplete(CommandSender sender) {
            return Lists.newArrayList(Objects.toString((int) Double.parseDouble(CoordinateDoubleX.getTabComplete(sender).get(0))));
        }

        boolean accept(String arg) {
            return integerPredicate.test(arg);
        }

        @Override
        Integer convert(String arg) {
            return Integer.parseInt(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Integer arg) {
            dataCollection.writeInt(arg);
        }
    };
    public static final TabCompleter<Integer> CoordinateIntY = new TabCompleter<Integer>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            return Lists.newArrayList(Objects.toString((int) Double.parseDouble(CoordinateDoubleY.getTabComplete(sender).get(0))));
        }

        @Override
        boolean accept(String arg) {
            return integerPredicate.test(arg);
        }

        @Override
        Integer convert(String arg) {
            return Integer.parseInt(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Integer arg) {
            dataCollection.writeInt(arg);
        }
    }; public static final TabCompleter<Integer> CoordinateIntZ= new TabCompleter<Integer>() {
        @Override
        List<String> getTabComplete(CommandSender sender) {
            return Lists.newArrayList(Objects.toString((int) Double.parseDouble(CoordinateDoubleZ.getTabComplete(sender).get(0))));
        }

        @Override
        boolean accept(String arg) {
            return integerPredicate.test(arg);
        }

        @Override
        Integer convert(String arg) {
            return Integer.parseInt(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Integer arg) {
            dataCollection.writeInt(arg);
        }
    }; public static final TabCompleter<String> PlayerName = new TabCompleter<String>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            for (World world : Bukkit.getWorlds())
                for (Player player : world.getPlayers())
                    ret.add(player.getName());
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return Bukkit.getPlayerExact(arg) != null;
        }

        @Override
        String convert(String arg) {
            return arg;
        }

        @Override
        void connect(DataCollection dataCollection, String arg) {
            dataCollection.writeString(arg);
        }
    }; public static final TabCompleter<UUID> PlayerUUID = new TabCompleter<UUID>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            for (World world : Bukkit.getWorlds())
                for (Player player : world.getPlayers())
                    ret.add(player.getUniqueId().toString());
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return UUIDPredicate.test(arg) && Bukkit.getPlayer(UUID.fromString(arg)) != null;
        }

        @Override
        UUID convert(String arg) {
            return UUID.fromString(arg);
        }

        @Override
        void connect(DataCollection dataCollection, UUID arg) {
            dataCollection.writeUUID(arg);
        }
    }; public static final TabCompleter<String> EntityName = new TabCompleter<String>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            for (World world : Bukkit.getWorlds())
                for (Entity entity : world.getEntities())
                    ret.add(entity.getName());
            return ret;
        }

        @Override
        boolean accept(String arg) {
            boolean flag = false;
            for (World world : Bukkit.getWorlds())
                for (Entity entity : world.getEntities())
                    if (entity.getName().equals(arg)) {
                        flag = true;
                        break;
                    }
            return flag;
        }

        @Override
        String convert(String arg) {
            return arg;
        }

        @Override
        void connect(DataCollection dataCollection, String arg) {
            dataCollection.writeString(arg);
        }
    }; public static final TabCompleter<UUID> EntityUUID = new TabCompleter<UUID>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            for (World world : Bukkit.getWorlds())
                for (Entity entity : world.getEntities())
                    ret.add(entity.getUniqueId().toString());
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return UUIDPredicate.test(arg) && Bukkit.getEntity(UUID.fromString(arg)) != null;
        }

        @Override
        UUID convert(String arg) {
            return UUID.fromString(arg);
        }

        @Override
        void connect(DataCollection dataCollection, UUID arg) {
            dataCollection.writeUUID(arg);
        }
    }; public static final TabCompleter<World> World = new TabCompleter<World>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            for (World world : Bukkit.getWorlds())
                ret.add(world.getName());
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return Bukkit.getWorld(arg) != null;
        }

        @Override
        World convert(String arg) {
            return Bukkit.getWorld(arg);
        }

        @Override
        void connect(DataCollection dataCollection, World arg) {
            dataCollection.writeWorld(arg);
        }
    }; public static final TabCompleter<Double> CoordinateDoubleX= new TabCompleter<Double>() {
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            if (sender instanceof Entity)
                ret.add(Objects.toString(((Entity) sender).getLocation().getX()));
            if (sender instanceof BlockCommandSender)
                ret.add(Objects.toString(((BlockCommandSender) sender).getBlock().getLocation().getX()));
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return doublePredicate.test(arg);
        }

        @Override
        Double convert(String arg) {
            return Double.parseDouble(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Double arg) {
            dataCollection.writeDouble(arg);
        }
    }; public static final TabCompleter<Double> CoordinateDoubleY = new TabCompleter<Double>(){
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            if (sender instanceof Entity)
                ret.add(Objects.toString(((Entity) sender).getLocation().getY()));
            if (sender instanceof BlockCommandSender)
                ret.add(Objects.toString(((BlockCommandSender) sender).getBlock().getLocation().getY()));
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return doublePredicate.test(arg);
        }

        @Override
        Double convert(String arg) {
            return Double.parseDouble(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Double arg) {
            dataCollection.writeDouble(arg);
        }
    }; public static final TabCompleter<Double> CoordinateDoubleZ = new TabCompleter<Double>() {
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = Lists.newArrayList();
            if (sender instanceof Entity)
                ret.add(Objects.toString(((Entity) sender).getLocation().getZ()));
            if (sender instanceof BlockCommandSender)
                ret.add(Objects.toString(((BlockCommandSender) sender).getBlock().getLocation().getZ()));
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return doublePredicate.test(arg);
        }

        @Override
        Double convert(String arg) {
            return Double.parseDouble(arg);
        }

        @Override
        void connect(DataCollection dataCollection, Double arg) {
            dataCollection.writeDouble(arg);
        }
    }; public static final TabCompleter<String> EntityExceptPlayerName = new TabCompleter<String>() {
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = EntityName.getTabComplete(sender);
            ret.removeAll(PlayerName.getTabComplete(sender));
            return ret;
        }

        @Override
        boolean accept(String arg) {
            boolean flag = false;
            for (World world : Bukkit.getWorlds())
                for (Entity entity : world.getEntities())
                    if (!(entity instanceof Player) && entity.getName().equals(arg)) {
                        flag = true;
                        break;
                    }
            return flag;
        }

        @Override
        String convert(String arg) {
            return arg;
        }

        @Override
        void connect(DataCollection dataCollection, String arg) {
            dataCollection.writeString(arg);
        }

    }; public static final TabCompleter<UUID> EntityExceptPlayerUUID = new TabCompleter<UUID>() {
        @Override
        List<String> getTabComplete(CommandSender sender) {
            List<String> ret = EntityUUID.getTabComplete(sender);
            ret.removeAll(PlayerUUID.getTabComplete(sender));
            return ret;
        }

        @Override
        boolean accept(String arg) {
            return EntityUUID.accept(arg) && Bukkit.getPlayer(UUID.fromString(arg)) == null;
        }

        @Override
        UUID convert(String arg) {
            return UUID.fromString(arg);
        }

        @Override
        void connect(DataCollection dataCollection, UUID arg) {
            dataCollection.writeUUID(arg);
        }
    };
    private static final Predicate<String> integerPredicate = i -> {
        try {
            Integer.parseInt(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    private static final Predicate<String> UUIDPredicate = i -> {
        try {
            UUID.fromString(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    private static final Predicate<String> doublePredicate = i -> {
        try {
            Double.parseDouble(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    abstract List<String> getTabComplete(CommandSender sender);

}