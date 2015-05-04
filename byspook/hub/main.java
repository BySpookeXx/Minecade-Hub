package byspook.hub;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
	getLogger().info("Starting Hub plugin v1.3! (Developed by BySpooK)");
	
	saveDefaultConfig();
    getServer().getPluginManager().registerEvents(this, this);
    
    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      private Random r = new Random();
      
  @SuppressWarnings("deprecation")
	public void run()
      {
        Color c = Color.fromRGB(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255));
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getServer().getOnlinePlayers()).length;
        for (int i = 0; i < j; i++)
        {
          Player p = arrayOfPlayer[i];
          if ((p.getInventory().getChestplate() != null) && (p.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE)) 
          {
            p.getInventory().setChestplate(main.this.getColorArmor(Material.LEATHER_CHESTPLATE, c));
          }
        }
      }
    }, 0L, 20L);
    
	getLogger().info("Hub plugin Started! (Developed by BySpooK)");
  }
  
  private ItemStack getColorArmor(Material m, Color c)
  {
    ItemStack i = new ItemStack(m, 1);
    LeatherArmorMeta meta = (LeatherArmorMeta)i.getItemMeta();
    meta.setColor(c);
    i.setItemMeta(meta);
    return i;
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e)
  {
    if (e.getPlayer().hasPermission("hub.extra.color.chestplate"))
    {
      e.getPlayer().getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
    }
    
    if (e.getPlayer().hasPermission("hub.extra.hat.glass"))
    {
      e.getPlayer().getInventory().setHelmet(new ItemStack(Material.GLASS));
    }
  }
  
  @EventHandler
  public void joinMessage(PlayerJoinEvent e)
  {
	if (e.getPlayer().hasPermission("hub.messages.join"))
	{
      Player p = e.getPlayer();
      e.setJoinMessage(getConfig().getString("JoinMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
    }

	if (e.getPlayer().hasPermission("hub.messages.join.vip"))
	{
      Player p = e.getPlayer();
      e.setJoinMessage(getConfig().getString("VIP-JoinMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
	}
	
	if (e.getPlayer().hasPermission("hub.messages.join.vip+"))
	{
      Player p = e.getPlayer();
      e.setJoinMessage(getConfig().getString("VIP+-JoinMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
	}
	
	if (e.getPlayer().hasPermission("hub.messages.join.staff"))
	{
      Player p = e.getPlayer();
      e.setJoinMessage(getConfig().getString("STAFF-JoinMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
	}
  }

  @EventHandler
  public void leaveMessage(PlayerQuitEvent e)
  {
	if (e.getPlayer().hasPermission("hub.messages.leave"))
	{
      Player p = e.getPlayer();
      e.setQuitMessage(getConfig().getString("LeaveMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
	}


	if (e.getPlayer().hasPermission("hub.messages.leave.vip"))
	{
      Player p = e.getPlayer();
      e.setQuitMessage(getConfig().getString("VIP-LeaveMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
    }
	
	if (e.getPlayer().hasPermission("hub.messages.leave.vip+"))
	{
      Player p = e.getPlayer();
      e.setQuitMessage(getConfig().getString("VIP+-LeaveMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
    }
	
	if (e.getPlayer().hasPermission("hub.messages.leave.staff"))
	{
      Player p = e.getPlayer();
      e.setQuitMessage(getConfig().getString("STAFF-LeaveMessage").replaceAll("&", "§").replaceAll("%player%", p.getName()));
    }
  }
  
@SuppressWarnings("deprecation")
  @EventHandler
  public void onDrop(PlayerDropItemEvent e)
  {
    Player p = e.getPlayer();
    
    if (p.isOp()) 
    {
      return;
    }
    
    if ((getConfig().getBoolean("DenyDrop")))
    {
      e.setCancelled(true);
      p.updateInventory();
      p.sendMessage(getConfig().getString("DropMessage").replaceAll("&", "§"));
      p.updateInventory();
      p.updateInventory();
      return;
    }
    
  }

  @EventHandler
  public void onRain(WeatherChangeEvent event)
  {
    if ((getConfig().getBoolean("DisableRain")) && (event.toWeatherState()))
      event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerLoseHunger(FoodLevelChangeEvent e)
  {
    if (getConfig().getBoolean("DisableHunger"))
      e.setFoodLevel(20);
  }
  
  @EventHandler
  public void onBreak(BlockBreakEvent e)
  {
    Player p = e.getPlayer();
    
    if (p.isOp()) 
    {
      return;
    }
    
    if (getConfig().getBoolean("DenyBuild"))
    {
      e.setCancelled(true);
      p.sendMessage(getConfig().getString("BuildMessage").replaceAll("&", "§"));
    }
  }
  
  @EventHandler
  public void onPlace(BlockPlaceEvent e)
  {
    Player p = e.getPlayer();
    
    if (p.isOp()) 
    {
      return;
    }
    
    if (getConfig().getBoolean("DenyBuild"))
    {
      e.setCancelled(true);
      p.sendMessage(getConfig().getString("BuildMessage").replaceAll("&", "§"));
    }
  }
  
@SuppressWarnings("deprecation")
@EventHandler
  public void onMove(PlayerMoveEvent e)
  {
	  Player p = e.getPlayer();
	  if(p.getGameMode() == GameMode.SURVIVAL);
	  {
		  if(!p.getAllowFlight())
		  {
			  if(p.isOnGround())
			  {
			  p.setAllowFlight(true);
			  }
		  }
	  }
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onDoubleJump(PlayerToggleFlightEvent e)
  {
	  Player p = e.getPlayer();
	  if ((p.getGameMode() == GameMode.SURVIVAL) && (getConfig().getBoolean("DoubleJump")) && (e.getPlayer().hasPermission("hub.extras.doublejump")))
	  {
		  e.setCancelled(true);
		  p.setAllowFlight(false);
		  p.setFlying(false);
		  p.setVelocity(p.getLocation().getDirection().multiply(getConfig().getInt("JumpPower")));
		  
		  if (getConfig().getBoolean("JumpSound"))
		  {
			  p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 5, 5);
		  }
		  
		  if (getConfig().getBoolean("JumpParticles"))
		  {
			  p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);
		  }	  
	  }
  }

  public void onDamage(EntityDamageEvent e)
  {
	if (getConfig().getBoolean("DisableFallDamage"))
	{
	   if (e.getEntity() instanceof Player)
	   {
		   e.setCancelled(true);
	   }
	}
  }
}
