package dev._2lstudios.skywars.game;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.entity.Player;

public class GameParty {
  private final GamePlayer owner;
  
  private final Collection<GamePlayer> invited = new HashSet<>();
  
  private final Collection<GamePlayer> members = new HashSet<>();
  
  public GameParty(GamePlayer owner) {
    this.owner = owner;
    this.members.add(owner);
  }
  
  public GamePlayer getOwner() {
    return this.owner;
  }
  
  public void disband() {
    for (GamePlayer gamePlayer : this.members)
      gamePlayer.setParty(null); 
    this.members.clear();
    this.invited.clear();
  }
  
  public boolean invite(GamePlayer gamePlayer) {
    if (!this.members.contains(gamePlayer))
      return this.invited.add(gamePlayer); 
    return false;
  }
  
  public boolean deinvite(GamePlayer gamePlayer) {
    if (this.members.contains(gamePlayer))
      return this.invited.remove(gamePlayer); 
    return false;
  }
  
  public boolean add(GamePlayer gamePlayer) {
    return this.members.add(gamePlayer);
  }
  
  public boolean remove(GamePlayer gamePlayer) {
    return this.members.remove(gamePlayer);
  }
  
  public void sendMessage(String message) {
    for (GamePlayer gamePlayer : this.members) {
      Player player = gamePlayer.getPlayer();
      player.sendMessage(message);
    } 
  }
  
  public Collection<GamePlayer> getMembers() {
    return this.members;
  }
  
  public Collection<GamePlayer> getInvited() {
    return this.invited;
  }
}
