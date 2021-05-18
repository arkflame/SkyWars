package dev._2lstudios.skywars.chest;

public enum ChestType {
  BASIC("Basico"),
  NORMAL("Normal"),
  INSANE("Insano");
  
  private final String name;
  
  ChestType(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
