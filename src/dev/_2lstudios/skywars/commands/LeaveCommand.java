package dev._2lstudios.skywars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class LeaveCommand implements CommandExecutor {
  private final GamePlayerManager playerManager;
  
  public LeaveCommand(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      Arena arena = gamePlayer.getArena();
      if (arena != null) {
        arena.remove(gamePlayer);
      } else {
        sender.sendMessage(ChatColor.RED + "No estas en ninguna arena!");
      } 
    } 
    return true;
  }
}
