package dev._2lstudios.skywars.time;

public enum TimeType {
  MORNING("Mañana"), DAY("Dia"), NOON("Tarde"), NIGHT("Noche");

  private final String name;

  private TimeType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
