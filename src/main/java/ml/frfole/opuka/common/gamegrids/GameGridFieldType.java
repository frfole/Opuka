package ml.frfole.opuka.common.gamegrids;

public enum GameGridFieldType {
  INVALID("--"),
  CLEAR("  "),
  NEAR_1(" 1"),
  NEAR_2(" 2"),
  NEAR_3(" 3"),
  NEAR_4(" 4"),
  NEAR_5(" 5"),
  NEAR_6(" 6"),
  NEAR_7(" 7"),
  NEAR_8(" 8"),
  MINE(" #"),
  UNKNOWN_CLEAR("++"),
  UNKNOWN_NEAR_1("+1"),
  UNKNOWN_NEAR_2("+2"),
  UNKNOWN_NEAR_3("+3"),
  UNKNOWN_NEAR_4("+4"),
  UNKNOWN_NEAR_5("+5"),
  UNKNOWN_NEAR_6("+6"),
  UNKNOWN_NEAR_7("+7"),
  UNKNOWN_NEAR_8("+8"),
  UNKNOWN_MINE("+#"),
  UNKNOWN_FLAG_CLEAR("^ "),
  UNKNOWN_FLAG_NEAR_1("^1"),
  UNKNOWN_FLAG_NEAR_2("^2"),
  UNKNOWN_FLAG_NEAR_3("^3"),
  UNKNOWN_FLAG_NEAR_4("^4"),
  UNKNOWN_FLAG_NEAR_5("^5"),
  UNKNOWN_FLAG_NEAR_6("^6"),
  UNKNOWN_FLAG_NEAR_7("^7"),
  UNKNOWN_FLAG_NEAR_8("^8"),
  UNKNOWN_FLAG_MINE("^#");

  private final String s;
  GameGridFieldType(String s) {
    this.s = s;
  }

  public String getS() {
    return this.s;
  }

  public boolean isUnknownNotMine() {
    return this == UNKNOWN_CLEAR || this == UNKNOWN_NEAR_1 || this == UNKNOWN_NEAR_2 || this == UNKNOWN_NEAR_3
            || this == UNKNOWN_NEAR_4 || this == UNKNOWN_NEAR_5 || this == UNKNOWN_NEAR_6 || this == UNKNOWN_NEAR_7
            || this == UNKNOWN_NEAR_8;
  }

  public boolean isUnknownNear() {
    return this == UNKNOWN_NEAR_1 || this == UNKNOWN_NEAR_2 || this == UNKNOWN_NEAR_3 || this == UNKNOWN_NEAR_4
            || this == UNKNOWN_NEAR_5 || this == UNKNOWN_NEAR_6 || this == UNKNOWN_NEAR_7 || this == UNKNOWN_NEAR_8;
  }

  public boolean isUnknown() {
    return isUnknownNotMine() || this == UNKNOWN_MINE;
  }

  public boolean isKnown() {
    return this == CLEAR || this == NEAR_1 || this == NEAR_2 || this == NEAR_3 || this == NEAR_4 || this == NEAR_5
            || this == NEAR_6 || this == NEAR_7 || this == NEAR_8 || this == MINE;
  }

  public boolean isFlagged() {
    return this == UNKNOWN_FLAG_CLEAR || this == UNKNOWN_FLAG_NEAR_1 || this == UNKNOWN_FLAG_NEAR_2
            || this == UNKNOWN_FLAG_NEAR_3 || this == UNKNOWN_FLAG_NEAR_4 || this == UNKNOWN_FLAG_NEAR_5
            || this == UNKNOWN_FLAG_NEAR_6 || this == UNKNOWN_FLAG_NEAR_7 || this == UNKNOWN_FLAG_NEAR_8
            || this == UNKNOWN_FLAG_MINE;
  }

  public GameGridFieldType unknownNextType() {
    switch (this) {
      case UNKNOWN_CLEAR: return UNKNOWN_NEAR_1;
      case UNKNOWN_NEAR_1: return UNKNOWN_NEAR_2;
      case UNKNOWN_NEAR_2: return UNKNOWN_NEAR_3;
      case UNKNOWN_NEAR_3: return UNKNOWN_NEAR_4;
      case UNKNOWN_NEAR_4: return UNKNOWN_NEAR_5;
      case UNKNOWN_NEAR_5: return UNKNOWN_NEAR_6;
      case UNKNOWN_NEAR_6: return UNKNOWN_NEAR_7;
      case UNKNOWN_NEAR_7: return UNKNOWN_NEAR_8;
      default: return INVALID;
    }
  }

  public GameGridFieldType unknownNear2Clear() {
    switch (this) {
      case UNKNOWN_NEAR_1: return NEAR_1;
      case UNKNOWN_NEAR_2: return NEAR_2;
      case UNKNOWN_NEAR_3: return NEAR_3;
      case UNKNOWN_NEAR_4: return NEAR_4;
      case UNKNOWN_NEAR_5: return NEAR_5;
      case UNKNOWN_NEAR_6: return NEAR_6;
      case UNKNOWN_NEAR_7: return NEAR_7;
      case UNKNOWN_NEAR_8: return NEAR_8;
      default: return INVALID;
    }
  }

  public GameGridFieldType unknown2Flagged() {
    switch (this) {
      case UNKNOWN_CLEAR: return  UNKNOWN_FLAG_CLEAR;
      case UNKNOWN_NEAR_1: return UNKNOWN_FLAG_NEAR_1;
      case UNKNOWN_NEAR_2: return UNKNOWN_FLAG_NEAR_2;
      case UNKNOWN_NEAR_3: return UNKNOWN_FLAG_NEAR_3;
      case UNKNOWN_NEAR_4: return UNKNOWN_FLAG_NEAR_4;
      case UNKNOWN_NEAR_5: return UNKNOWN_FLAG_NEAR_5;
      case UNKNOWN_NEAR_6: return UNKNOWN_FLAG_NEAR_6;
      case UNKNOWN_NEAR_7: return UNKNOWN_FLAG_NEAR_7;
      case UNKNOWN_NEAR_8: return UNKNOWN_FLAG_NEAR_8;
      case UNKNOWN_MINE: return UNKNOWN_FLAG_MINE;
      default: return INVALID;
    }
  }

  public GameGridFieldType flagged2Unknown() {
    switch (this) {
      case UNKNOWN_FLAG_CLEAR:  return UNKNOWN_CLEAR;
      case UNKNOWN_FLAG_NEAR_1: return UNKNOWN_NEAR_1;
      case UNKNOWN_FLAG_NEAR_2: return UNKNOWN_NEAR_2;
      case UNKNOWN_FLAG_NEAR_3: return UNKNOWN_NEAR_3;
      case UNKNOWN_FLAG_NEAR_4: return UNKNOWN_NEAR_4;
      case UNKNOWN_FLAG_NEAR_5: return UNKNOWN_NEAR_5;
      case UNKNOWN_FLAG_NEAR_6: return UNKNOWN_NEAR_6;
      case UNKNOWN_FLAG_NEAR_7: return UNKNOWN_NEAR_7;
      case UNKNOWN_FLAG_NEAR_8: return UNKNOWN_NEAR_8;
      case UNKNOWN_FLAG_MINE:   return UNKNOWN_MINE;
      default: return INVALID;
    }
  }
}
