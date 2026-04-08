package me.tim.managerplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class ManagerPlugin extends JavaPlugin implements Listener {
    // I don't even know what an Implements are IntelliJ just forced me to add this for the event handler to work

    private boolean funny = false;
    public static HashSet<UUID> frozenPlayers = new HashSet<>();
    Set<String> allowed = Set.of("/freeze", "/help", "/msg");

    // should I of  made a separate class for this yes am I to lazy yes
    // I hate Java
    // I also hate GitHub
    // I also hate Gradle it is so big and chunky to use

    @Override
    public void onEnable() {
        System.out.println("Manager Plugin Enabled");
        getServer().getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
    }

    //commands can be added later because I kinda am on holiday

    //what is the point of this method this sucks if the plugin is being disabled why would I do anything this method makes no sense because the plugin manager does all the stuff for you whoever made this method is such a melon bro
    @Override
    public void onDisable() {
        System.out.println("Manager Plugin Disabled");
    }

    @EventHandler
    public void onEveryCommand(PlayerCommandPreprocessEvent event)
    {
        Player p = event.getPlayer();
        if(frozenPlayers.contains(p.getUniqueId()))
        {
            String command = event.getMessage().split(" ")[0].toLowerCase();
            if(!allowed.contains(command))
            {
                p.sendMessage(ChatColor.RED + "Some Commands are not allowed while frozen");
                event.setCancelled(true);
            }
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(cmd.getName().equalsIgnoreCase("setcourt"))
        {
            Player p = (Player) sender;
            if(!p.isOp())
            {
                p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            }
            else
            {
                Location location = p.getLocation();
                FileConfiguration config = this.getConfig();

                config.set("court.world", location.getWorld().getName());
                config.set("court.x", location.getBlockX());
                config.set("court.y", location.getBlockY());
                config.set("court.z", location.getBlockZ());
                config.set("court.yaw", location.getYaw());
                config.set("court.pitch", location.getPitch());

                this.saveConfig();
                p.sendMessage(ChatColor.GREEN + "Court has been set at X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
            }
        }

        if(cmd.getName().equalsIgnoreCase("court"))
        {
            Player p = (Player) sender;
            if(!p.isOp())
            {
                p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                return true;
            }

            if(args.length == 0)
            {
                p.sendMessage(ChatColor.RED + "Usage: /court <player>");
            }
            else
            {
                String playerName = args[0];
                Player target = Bukkit.getServer().getPlayerExact(playerName);

                if(target == null)
                {
                    p.sendMessage(ChatColor.RED + "Player not found");
                }
                else
                {
                    FileConfiguration config = this.getConfig();
                    String worldName = config.getString("court.world");
                    if(worldName == null)
                    {
                        p.sendMessage(ChatColor.RED + "World name not found in config");
                        return true;
                    }

                    World w = Bukkit.getWorld(worldName);
                    if(w == null)
                    {
                        p.sendMessage(ChatColor.RED + "World not found");
                        return true;
                    }

                    int x = config.getInt("court.x");
                    int y = config.getInt("court.y");
                    int z = config.getInt("court.z");
                    float yaw =  (float)config.getDouble("court.yaw");
                    float pitch = (float) config.getDouble("court.pitch");
                    target.teleport(new Location(w, x, y, z, yaw, pitch));

                }
            }
        }


        if (cmd.getName().equalsIgnoreCase("Freeze"))
        {
            if(sender instanceof Player p )
            {
                if(!p.isOp())
                {
                    p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                    return true;
                }

                if(args.length == 0)
                {
                    p.sendMessage(ChatColor.RED + "Usage: /Freeze <player>");
                }
                else
                {
                    String playerName = args[0];
                    Player target = Bukkit.getServer().getPlayerExact(playerName);

                    if(target == null)
                    {
                        p.sendMessage(ChatColor.RED + "Player not found");
                    }
                    else
                    {
                        if(frozenPlayers.contains(target.getUniqueId()))
                        {
                            frozenPlayers.remove(target.getUniqueId());
                            p.sendMessage(ChatColor.GREEN + target.getName() + " has been Unfrozen");
                        }
                        else
                        {
                            frozenPlayers.add(target.getUniqueId());
                            p.sendMessage(ChatColor.GREEN + "Frozen " + target.getName());
                        }
                    }
                }
            }


        }


        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        if(frozenPlayers.contains(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(frozenPlayers.contains(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        //please change funny to true 🙏
        if(funny)
        {
            if ((event.getPlayer().getName().equals("TalllTim")))
            {
                getServer().broadcastMessage("The Best Player has Joined");
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        // these Docs suck so much it took me 20 mins to find this stupid event I wish the worst upon the people who made these docs
        Player player = event.getEntity();
        // jave file stuff is actually really nice this is the only good thing I can say about java
        try
        {
            //writes to a file that's pretty much it
            //intellij gave me a spelling error for the line above and wouldn't let be build when it's a comment
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt",true));
            String deathMessage = event.getDeathMessage();
            writer.write(player.getName() + " Died lol" + "\n");
            writer.write("Cause was: " + deathMessage + "\n");
            writer.write("Inventory contents:" + "\n");
            //for loop that goes through each players item
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    writer.write(item.toString() + "\n");
                }
            }
            //some \n so it looks nice
            writer.write("End of Inv Stuff \n \n \n");
            writer.close();
        }
        //catch because intellij forced me to do this I doubt this will have errors since im the best programmer ever
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
