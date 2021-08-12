package dev._2lstudios.skywars.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.game.player.GameParty;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;

public class PartyCommand implements CommandExecutor {
  private final Server server;

  private final PlayerManager playerManager;

  private final String helpMessage;

  public PartyCommand(Server server, PlayerManager playerManager) {
    this.server = server;
    this.playerManager = playerManager;
    this.helpMessage = ChatColor.GREEN + "Comandos de party:\n" + ChatColor.YELLOW + "/%label%" + " invite <jugador>"
        + ChatColor.GRAY + " - " + ChatColor.AQUA + "Invita a un jugador a tu party!\n" + ChatColor.YELLOW + "/%label%"
        + " deinvite <jugador>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Desinvita a un jugador a tu party!\n"
        + ChatColor.YELLOW + "/%label%" + " kick <jugador>" + ChatColor.GRAY + " - " + ChatColor.AQUA
        + "Quita a un jugador a tu party!\n" + ChatColor.YELLOW + "/%label%" + " join <jugador>" + ChatColor.GRAY
        + " - " + ChatColor.AQUA + "Unete a una party de un jugador!\n" + ChatColor.YELLOW + "/%label%"
        + " info [jugador]" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Informacion de la party!\n" + ChatColor.YELLOW
        + "/%label%" + " leave" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Salir de la party actual!\n"
        + ChatColor.YELLOW + "/%label%" + " disband" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Elimina tu party!";
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      int length = args.length;
      if (length == 0 || args[0].equals("help")) {
        sender.sendMessage(this.helpMessage.replace("%label%", label));
      } else if (args[0].equals("invite")) {
        if (length > 1) {
          GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
          GamePlayer gamePlayer1 = this.playerManager.getPlayer(this.server.getPlayer(args[1]));
          if (gamePlayer1 != null) {
            if (gamePlayer1 != gamePlayer) {
              GameParty party = gamePlayer.getParty();
              if (party != null) {
                if (party.getOwner() == gamePlayer) {
                  if (party.invite(gamePlayer1)) {
                    gamePlayer1.sendMessage(ChatColor.GREEN + "Fuiste invitado a la party de " + sender.getName()
                        + "! Usa /party para ver comandos!");
                    sender.sendMessage(ChatColor.GREEN + "Invitaste a " + args[1] + " a tu party!");
                  } else {
                    sender.sendMessage(ChatColor.RED + "El jugador ya esta invitado a tu party!");
                  }
                } else {
                  sender.sendMessage(ChatColor.RED + "No eres el lider de la party!");
                }
              } else {
                GameParty newParty = new GameParty(gamePlayer);
                gamePlayer.setParty(newParty);
                if (newParty.invite(gamePlayer1)) {
                  gamePlayer1.sendMessage(ChatColor.GREEN + "Fuiste invitado a la party de " + sender.getName()
                      + "! Usa /party para ver comandos!");
                  sender.sendMessage(ChatColor.GREEN + "Invitaste a " + args[1] + " a tu party!");
                } else {
                  sender.sendMessage(ChatColor.RED + "El jugador ya esta invitado a tu party!");
                }
              }
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes invitarte a ti mismo!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "El jugador solicitado no existe!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "/party invite <jugador>");
        }
      } else if (args[0].equals("deinvite")) {
        if (length > 1) {
          GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
          GamePlayer gamePlayer1 = this.playerManager.getPlayer(this.server.getPlayer(args[1]));
          if (gamePlayer1 != null) {
            if (gamePlayer1 != gamePlayer) {
              GameParty party = gamePlayer.getParty();
              if (party != null) {
                if (party.getOwner() == gamePlayer) {
                  if (party.deinvite(gamePlayer)) {
                    sender.sendMessage(ChatColor.GREEN + "Desinvitaste a " + args[1] + " de tu party!");
                  } else {
                    sender.sendMessage(ChatColor.RED + "El jugador no esta invitado a la party!");
                  }
                } else {
                  sender.sendMessage(ChatColor.RED + "No eres el lider de la party!");
                }
              } else {
                sender.sendMessage(ChatColor.RED + "No estas en ninguna party!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes desinvitarte a ti mismo!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "El jugador solicitado no existe!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "/party deinvite <jugador>");
        }
      } else if (args[0].equals("kick")) {
        if (length > 1) {
          GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
          GamePlayer gamePlayer1 = this.playerManager.getPlayer(this.server.getPlayer(args[1]));
          if (gamePlayer1 != null) {
            GameParty party = gamePlayer.getParty();
            if (party != null) {
              if (party.getOwner() == gamePlayer) {
                party.remove(gamePlayer1);
                party.sendMessage(ChatColor.RED + gamePlayer1.getPlayer().getName() + " fue kickeado de la party!");
                gamePlayer1.sendMessage(ChatColor.RED + "Fuiste kickeado de la party!");
              } else {
                sender.sendMessage(ChatColor.RED + "No eres el lider de la party!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "No estas en ninguna party!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "El jugador solicitado no existe!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "/party kick <jugador>");
        }
      } else if (args[0].equals("join")) {
        if (length > 1) {
          GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
          GamePlayer gamePlayer1 = this.playerManager.getPlayer(this.server.getPlayer(args[1]));
          if (gamePlayer != gamePlayer1) {
            GameParty playerParty = gamePlayer.getParty();
            if (playerParty != null) {
              playerParty.remove(gamePlayer);
              playerParty
                  .sendMessage(ChatColor.RED + "La party donde estabas fue eliminada por " + sender.getName() + "!");
              playerParty.disband();
              sender.sendMessage(ChatColor.GREEN + "Eliminaste tu party correctamente!");
            }
            GameParty party = gamePlayer1.getParty();
            if (party != null) {
              if (party.getInvited().contains(gamePlayer)) {
                gamePlayer.setParty(party);
                party.deinvite(gamePlayer);
                party.sendMessage(ChatColor.GREEN + gamePlayer.getPlayer().getName() + " se unio a la party!");
                sender.sendMessage(ChatColor.GREEN + "Te uniste a la party correctamente!");
              } else {
                sender.sendMessage(ChatColor.RED + "No estas invitado a esa party!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "La party solicitada no existe!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "No puedes unirte a tu propia party!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "/party join <jugador>");
        }
      } else if (args[0].equals("info")) {
        GamePlayer gamePlayer;
        if (length > 1) {
          gamePlayer = this.playerManager.getPlayer(this.server.getPlayer(args[1]));
        } else {
          gamePlayer = this.playerManager.getPlayer((Player) sender);
        }
        if (gamePlayer != null) {
          GameParty party = gamePlayer.getParty();
          if (party != null) {
            StringBuilder partyPlayersString = new StringBuilder();

            for (GamePlayer member : party.getMembers())
              partyPlayersString.append("&a" + member.getPlayer().getName() + "&7, ");
            partyPlayersString.delete(partyPlayersString.length() - 2, partyPlayersString.length());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInformacion de la Party:\n&9Lider: &a"
                + party.getOwner().getPlayer().getName() + "\n&9Miembros: " + partyPlayersString));
          } else {
            sender.sendMessage(ChatColor.RED + "No estas en ninguna party!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "El jugador solicitado no existe!");
        }
      } else if (args[0].equals("leave")) {
        GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
        GameParty party = gamePlayer.getParty();
        if (party != null) {
          party.remove(gamePlayer);
          if (party.getOwner() == gamePlayer) {
            party.remove(gamePlayer);
            party.sendMessage(ChatColor.RED + "La party donde estabas fue eliminada por " + sender.getName() + "!");
            party.disband();
            sender.sendMessage(ChatColor.GREEN + "Eliminaste tu party correctamente!");
          } else {
            party.sendMessage(ChatColor.RED + sender.getName() + " salio de la party!");
            sender.sendMessage(ChatColor.GREEN + "Saliste de tu party correctamente!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No estas en ninguna party!");
        }
      } else if (args[0].equals("disband")) {
        GamePlayer gamePlayer = this.playerManager.getPlayer((Player) sender);
        GameParty party = gamePlayer.getParty();
        if (party != null) {
          if (party.getOwner() == gamePlayer) {
            party.remove(gamePlayer);
            party.sendMessage(ChatColor.RED + "La party donde estabas fue eliminada por " + sender.getName() + "!");
            party.disband();
            sender.sendMessage(ChatColor.GREEN + "Eliminaste tu party correctamente!");
          } else {
            sender.sendMessage(ChatColor.RED + "No eres el lider de la party!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No estas en ninguna party!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Comando desconocido. Usa /party para ver una lista de comandos!");
      }
    }
    return true;
  }
}
