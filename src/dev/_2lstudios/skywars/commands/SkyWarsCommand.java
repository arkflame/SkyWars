package dev._2lstudios.skywars.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.ArenaManager;
import dev._2lstudios.skywars.managers.PlayerManager;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class SkyWarsCommand implements CommandExecutor {
  private final Server server;
  private final SkyWars skywars;
  private final ArenaManager arenaManager;
  private final PlayerManager playerManager;

  public SkyWarsCommand(Server server, SkyWars skywars, ArenaManager arenaManager, PlayerManager playerManager) {
    this.server = server;
    this.skywars = skywars;
    this.arenaManager = arenaManager;
    this.playerManager = playerManager;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (args.length == 0) {
        player.sendMessage(ChatColor.GREEN + "Comandos de SkyWars:"
            .concat(System.lineSeparator() + ChatColor.YELLOW + "/" + label + " join [mapa]" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Unete a una partida al azar!")
            .concat(System.lineSeparator() + ChatColor.YELLOW + "/" + label + " spec <mapa>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Espectea un mapa que especifiques!")
            .concat(System.lineSeparator() + ChatColor.YELLOW + "/" + label + " leave" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Sal del mapa que estas jugando!")
            .concat(System.lineSeparator() + ChatColor.YELLOW + "/" + label + " list" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Muestra una lista de mapas!"));
      } else if (args[0].equalsIgnoreCase("join")) {
        if (args.length == 2) {
          String arenaName = args[1];
          GameArena gameArena = this.arenaManager.getArena(arenaName);
          if (gameArena != null) {
            gameArena.addPlayer(gamePlayer);
          } else {
            player.sendMessage(ChatColor.RED + "La arena especificada no existe!");
          } 
        } else {
          GameArena gameArena = this.arenaManager.getMaxPlayerAvailableArena();
          if (gameArena != null) {
            player.sendMessage(ChatColor.GREEN + "Entrando a una partida al azar!");
            gameArena.addPlayer(gamePlayer);
          } else {
            player.sendMessage(ChatColor.RED + "No hay arenas configuradas!");
          } 
        } 
      } else if (args[0].equalsIgnoreCase("spec") && args.length == 2) {
        String arenaName = args[1];
        GameArena specGameArena = this.arenaManager.getArena(arenaName);
        if (specGameArena != null) {
          if (gamePlayer.getArena() == null) {
            specGameArena.addSpectator(gamePlayer);
          } else {
            player.sendMessage(ChatColor.RED + "No puedes espectar estando en juego!");
          } 
        } else {
          Player specPlayer = this.server.getPlayerExact(arenaName);
          if (specPlayer != null) {
            GamePlayer specGamePlayer = this.playerManager.getPlayer(specPlayer);
            if (specGamePlayer != null) {
              specGameArena = specGamePlayer.getArena();
              if (specGameArena != null) {
                specGameArena.addSpectator(gamePlayer);
              } else {
                player.sendMessage(ChatColor.RED + "El jugador no esta en juego!");
              } 
            } else {
              player.sendMessage(ChatColor.RED + "La arena especificada no existe!");
            } 
          } else {
            player.sendMessage(ChatColor.RED + "La arena especificada no existe!");
          } 
        } 
      } else if (args[0].equalsIgnoreCase("leave")) {
        GameArena gameArena = gamePlayer.getArena();
        if (gameArena != null) {
          gameArena.remove(gamePlayer);
        } else {
          player.sendMessage(ChatColor.RED + "No estas en ninguna arena!");
        } 
      } else if (args[0].equalsIgnoreCase("list")) {
        player.sendMessage(ChatColor.BLUE + "Arenas Creadas:");
        for (GameArena gameArena : this.arenaManager.getGameArenasAsList()) {
          if (gameArena.getState() == GameState.EDITING || gameArena.getSpawns().size() == 0) {
            player.sendMessage(ChatColor.RED + gameArena.getName());
            continue;
          } 
          player.sendMessage(ChatColor.GREEN + gameArena.getName());
        } 
      } else if (player.hasPermission("skywars.admin")) {
        if (args[0].equalsIgnoreCase("create") && args.length == 2) {
          String arenaName = args[1];
          if (this.arenaManager.getArena(arenaName) == null) {
            BukkitUtil.runSync(this.skywars, () -> {
              this.arenaManager.addGameArena(() -> {
                player.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " creada correctamente!");
              }, arenaName);
            });
          } else {
            player.sendMessage(ChatColor.RED + "La arena ya existe!");
          } 
        } else if (args[0].equalsIgnoreCase("delete")) {
          String arena = args[1];
          if (this.arenaManager.getArena(arena) != null) {
            BukkitUtil.runAsync(this.skywars, () -> {
                  this.arenaManager.removeGameArena(arena);
                  player.sendMessage(ChatColor.GREEN + "La arena se elimino correctamente!");
                });
          } else {
            player.sendMessage(ChatColor.RED + "La arena no existe!");
          } 
        } else if (args[0].equalsIgnoreCase("edit")) {
          String arena = args[1];
          GameArena gameArena = this.arenaManager.getArena(arena);
          if (gameArena != null) {
            gameArena.setState(() -> {
                  World world = gameArena.getWorld();
                  if (world != null) {
                    Location location = world.getSpawnLocation();
                    player.teleport(location.add(0.0D, 1.0D, 0.0D));
                    player.sendMessage(ChatColor.GREEN + "Estas editando la arena " + arena + "!");
                  } else {
                    player.sendMessage(ChatColor.RED + "The arena world isn't loaded!");
                  } 
                }, GameState.EDITING);
          } else {
            player.sendMessage(ChatColor.RED + "La arena no existe!");
          } 
        } else if (args[0].equalsIgnoreCase("save")) {
          String arena = player.getWorld().getName();
          if (this.arenaManager.getArena(arena) != null && this.arenaManager
            .getArena(arena).getState() == GameState.EDITING) {
            this.arenaManager.getArena(arena).setState(null, GameState.WAITING);
            player.sendMessage(ChatColor.GREEN + "La arena se guardo correctamente!");
          } else {
            player.sendMessage(ChatColor.RED + "La arena no esta en modo edicion!");
          } 
        } else if (args[0].equalsIgnoreCase("sign")) {
          if (args.length > 1) {
            if (args[1].equalsIgnoreCase("add")) {
              // TODO: Add GameSign
              player.sendMessage(ChatColor.GREEN + "Se agrego el bloque como cartel de Skywars!");
            } else if (args[1].equalsIgnoreCase("remove")) {
              // TODO: Remove GameSign
              player.sendMessage(ChatColor.RED + "Se elimino el bloque como cartel de SkyWars!");
            } else {
              player.sendMessage(ChatColor.RED + args[1] + " no es un argumento valido!");
            }
          } else {
            player.sendMessage(ChatColor.RED + "/" + label + " " + args[0] + " add/remove");
          }
        } else {
          player.sendMessage(ChatColor.RED + args[0] + " no es un argumento valido!");
        } 
      } else {
        player.sendMessage(ChatColor.RED + "Comando de SkyWars incorrecto!");
      } 
    } 
    return true;
  }
}
