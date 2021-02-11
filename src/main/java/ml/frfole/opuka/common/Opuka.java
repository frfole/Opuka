package ml.frfole.opuka.common;

public class Opuka {
  public final OpukaMethods methods;
  private static Opuka instance;

  public Opuka(OpukaMethods methods) {
    this.methods = methods;
    Opuka.instance = this;
  }

  public static Opuka getInstance() {
    return Opuka.instance;
  }
}
