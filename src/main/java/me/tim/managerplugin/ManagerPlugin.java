package me.tim.managerplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ManagerPlugin extends JavaPlugin implements Listener {
    // I don't even know what an Implements are IntelliJ just forced me to add this for the event handler to work

    private boolean funny = false;

    // should I of  made a separate class for this yes am I to lazy yes
    // I hate Java
    // I also hate GitHub
    // I also hate Gradle it is so big and chunky to use

    @Override
    public void onEnable() {
        System.out.println("Manager Plugin Enabled");
        getServer().getPluginManager().registerEvents(this,this);
    }

    //commands can be added later because I kinda am on holiday

    //what is the point of this method this sucks if the plugin is being disabled why would I do anything this method makes no sense because the plugin manager does all the stuff for you whoever made this method is such a melon bro
    @Override
    public void onDisable() {
        System.out.println("Manager Plugin Disabled");
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
