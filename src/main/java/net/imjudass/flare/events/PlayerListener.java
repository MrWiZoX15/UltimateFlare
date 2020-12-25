package net.imjudass.flare.events;

import net.imjudass.flare.Main;
import net.imjudass.flare.events.utils.ItemBuilder;
import net.imjudass.flare.packet.*;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayerListener implements Listener {

    public Main main;

    public PlayerListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();

        if (action == Action.RIGHT_CLICK_BLOCK
                && player.getItemInHand().getItemMeta() != null
                && player.getItemInHand().getItemMeta().getDisplayName() != null
                && player.getItemInHand().getItemMeta().getDisplayName().contains(main.getMesssage("Messages.name-item"))) {

            if (player.getItemInHand().getAmount() > 1) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            } else {
                player.getInventory().setItemInHand(null);
            }


            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 10, 0), player);
                }
            },10*1);

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 8, 0), player);
                }
            },3*1);

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 6, 0), player);
                }
            },3*2);

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 4, 0), player);
                }
            },3*3);

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 2.5, 0), player);
                }
            },3*4);

            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    spawnFirework(event.getClickedBlock().getLocation().add(0, 2.5, 0), player);

                    event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.CHEST);

                    Location loc = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getLocation();
                    Chest chest = (Chest) loc.getBlock().getState();

                    int setupMax = main.getConfig().getInt("Setup.items-in-chest");

                    for (int i = 0; i <= setupMax; i++) {
                        Random r = new Random();
                        int randomNum = r.nextInt(9 * 3) - 0;

                        int size = main.itemsList.size();
                        int random = new Random().nextInt(size);

                        Material material = main.itemsList.get(random);
                        String name = main.getConfig().getString("Items." + material + ".Name");
                        int amount = main.getConfig().getInt("Items." + material + ".Amount");
                        ArrayList<String> lore = (ArrayList<String>) main.getConfig().getList("Items." + material + ".Lore");

                        chest.getInventory().setItem(randomNum, new ItemBuilder(material).setName(name).setLore(lore).setAmount(amount).toItemStack());
                    }
                }
            },3*4);

        }

    }

    public static void spawnFirework(Location location, Player...players){
        int entityId=getNextEntityId();

        //Made Hermanos
        new PacketHandlerSpawnEntity(entityId, 76/*firework entityId*/, location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(), 0, 0, 0, 0).send(players);

        ItemStack fireworkItem=new ItemStack(Material.FIREWORK);
        FireworkMeta meta=(FireworkMeta)fireworkItem.getItemMeta();
        FireworkEffect effects = FireworkEffect.builder()
                .flicker(true)
                .withColor(Color.YELLOW)
                .withFade(Color.FUCHSIA)
                .with(FireworkEffect.Type.BALL)
                .trail(true)
                .build();
        meta.addEffect(effects);
        fireworkItem.setItemMeta(meta);

        scWatchableObject[] metadata=new scWatchableObject[]{
                new scWatchableObject(5, 8, toNMSItemStack(fireworkItem)),
                new scWatchableObject(0, 4, (byte)0),
                new scWatchableObject(0, 3, (byte)0),
                new scWatchableObject(4, 2, ""),
                new scWatchableObject(1, 1, (short)300),
                new scWatchableObject(0, 0, (byte)0)
        };

        new PacketHandlerEntityMetadata(entityId, Arrays.asList(metadata)).send(players);

        new PacketHandlerEntityStatus(entityId, (byte)17/*Explode Status*/).send(players);

        new PacketHandlerEntityDestroy(entityId).send(players);
    }

    private static int currentEntityId=Integer.MAX_VALUE;
    public static int getNextEntityId(){
        return currentEntityId++;
    }

    private static Method asNMSCopy;
    static{
        try{
            asNMSCopy=Class.forName("org.bukkit.craftbukkit."+
                    Bukkit.getServer().getClass().getName().split("\\.")[3]+
                    ".inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class);
        }catch(Exception a){
            a.printStackTrace();
        }
    }
    public static Object toNMSItemStack(ItemStack item){
        try{
            return asNMSCopy.invoke(null, item);
        }catch(Exception a){
            a.printStackTrace();
            return null;
        }
    }


}
