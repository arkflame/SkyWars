package dev._2lstudios.skywars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;

public class LeaveCommand implements CommandExecutor {
  private final PlayerManager playerManager;
  
  public LeaveCommand(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      GameArena gameArena = gamePlayer.getArena();
      if (gameArena != null) {
        gameArena.remove(gamePlayer);
      } else {
        sender.sendMessage(ChatColor.RED + "No estas en ninguna arena!");
      } 
    } 
    return true;
  }
}
