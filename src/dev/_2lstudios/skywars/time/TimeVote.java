package dev._2lstudios.skywars.time;

import java.util.UUID;

public class TimeVote {
  private final UUID uuid;
  
  private final TimeType timeType;
  
  public TimeVote(UUID uuid, TimeType timeType) {
    this.uuid = uuid;
    this.timeType = timeType;
  }
  
  public TimeType getType() {
    return this.timeType;
  }
  
  public UUID getUUID() {
    return this.uuid;
  }
}
