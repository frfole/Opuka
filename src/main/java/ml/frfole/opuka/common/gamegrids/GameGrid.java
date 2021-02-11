package ml.frfole.opuka.common.gamegrids;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GameGrid {
  private final int height;
  private final int width;
  protected FieldType[][] grid;
  protected Random random;
  private int invalidCount = 0;
  private int minesCount = 0;
  private State state = State.FINISHED_OTHER;
  protected final byte[][] nearSearch = new byte[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

  public GameGrid(int height, int width) {
    this.height = height;
    this.width = width;
    grid = new FieldType[height][width];
    prepareGrid();
  }

  /**
   * Sets all values of {@link #grid} to {@code 0}.
   */
  private void prepareGrid() {
    minesCount = 0;
    invalidCount = 0;
    for (int y = 0; y < height; y++)
      for (int x = 0; x < width; x++)
        grid[y][x] = FieldType.UNKNOWN_CLEAR;
    state = State.READY;
  }

  /**
   * Resets {@link GameGrid}.
   */
  public void reset() {
    state = State.READY;
    int mc = minesCount;
    minesCount = 0;
    for (int y = 0; y < height; y++)
      for (int x = 0; x < width; x++)
        if (grid[y][x] != FieldType.INVALID)
          grid[y][x] = FieldType.UNKNOWN_CLEAR;
    populateWithMines(mc);
  }

  /**
   * Sets field at x, y as invalid.
   * @param x the x
   * @param y the y
   */
  public void setInvalid(int x, int y) {
    if (!(0 <= x && x < height && 0 <= y && y < width)) return;
    if (grid[y][x] == FieldType.INVALID) return;
    grid[y][x] = FieldType.INVALID;
    invalidCount += 1;
  }

  /**
   * Populates {@link #grid} with mines and all fields with corresponding number of mines nearby.
   * @param count the amount of mines
   */
  public void populateWithMines(int count) {
    if (state != State.READY) return;
    if (count > width * height - invalidCount) return;
    int c = minesCount;
    while (minesCount - c < count) {
      int x = this.random.nextInt(width);
      int y = this.random.nextInt(height);
      if (grid[y][x] == FieldType.UNKNOWN_MINE || grid[y][x] == FieldType.INVALID) continue;
      minesCount += 1;
      grid[y][x] = FieldType.UNKNOWN_MINE;
      int cx, cy;
      for (byte[] i : nearSearch) {
        cx = x + i[0];
        cy = y + i[1];
        if (isIn(cx, cy) && grid[cy][cx] != FieldType.UNKNOWN_MINE)
          grid[cy][cx] = grid[cy][cx].unknownNextType();
      }
    }
  }

  /**
   * Dig field at x, and y. If mine is not at given x and y, clear nearby fields.
   * @param x the x
   * @param y the y
   */
  public void dig(int x, int y) {
    if (!(state == State.READY || state == State.PLAYING)) return;
    if (isOut(x, y)) return;
    if (grid[y][x].isUnknownNear()) {
      grid[y][x] = grid[y][x].unknown2Clear();
    }
    else if (grid[y][x] == FieldType.MINE) {
      state = State.FINISHED_MINE;
      return;
    }
    else if (grid[y][x] == FieldType.UNKNOWN_CLEAR) {
      grid[y][x] = FieldType.CLEAR;
      for (int cx = x - 1; cx < x + 2; cx++)
        for (int cy = y - 1; cy < y + 2; cy++)
          if (isIn(cx, cy) && grid[cy][cx].isUnknownNotMine())
            this.dig(cx, cy);
    }
    state = isDone() ? State.FINISHED_WIN : State.PLAYING;
  }

  /**
   * Mark field at x and y as possible field with mine.
   * @param x the x
   * @param y the y
   */
  public void flag(int x, int y) {
    if (isOut(x, y)) return;
    if (grid[y][x].isUnknown()) {
      grid[y][x] = grid[y][x].unknown2Flagged();
    } else if (grid[y][x].isFlagged()) {
      grid[y][x] = grid[y][x].flagged2Unknown();
    }
  }

  public boolean isOut(int x, int y) {
    return x < 0 || x > width - 1 || y < 0 || y > height - 1;
  }

  public boolean isIn(int x, int y) {
    return x > -1 && x < width && y > -1 && y < height;
  }


  /**
   * Returns the height of {@link GameGrid}.
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the width of {@link GameGrid}.
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the {@link #grid}.
   * @return the {@link #grid}
   */
  public FieldType[][] getGrid() {
    return grid;
  }

  /**
   * Checks if is all mines discovered.
   * @return {@code true} if all mines is discovered
   */
  public boolean isDone() {
    AtomicInteger known = new AtomicInteger();
    Arrays.stream(grid).forEach(fieldTypes -> Arrays.stream(fieldTypes).forEach(fieldType -> known.addAndGet(fieldType.isKnown() ? 1 : 0)));
    return width * height - known.get() - invalidCount == minesCount;
  }

  /**
   * Gets current {@link State} of this {@link GameGrid}.
   * @return current {@link State}
   */
  public State getState() {
    return state;
  }

  /**
   * Gets count of mines.
   * @return count of mines
   */
  public int getMinesCount() {
    return minesCount;
  }

  /**
   * Possible types of fields in {@link GameGrid}.
   */
  public enum FieldType {
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

    /**
     * Used to for printing.
     */
    public final String s;

    FieldType(String s) {
      this.s = s;
    }

    /**
     * Checks if {@link FieldType} is unknown and not mine.
     * @return {@code true} if {@link FieldType} is unknown and not mine, {@code false} otherwise
     */
    public boolean isUnknownNotMine() {
      return this == UNKNOWN_CLEAR || this == UNKNOWN_NEAR_1 || this == UNKNOWN_NEAR_2 || this == UNKNOWN_NEAR_3
              || this == UNKNOWN_NEAR_4 || this == UNKNOWN_NEAR_5 || this == UNKNOWN_NEAR_6 || this == UNKNOWN_NEAR_7
              || this == UNKNOWN_NEAR_8;
    }

    /**
     * Checks if {@link FieldType} is unknown and not {@link #UNKNOWN_MINE} or {@link #UNKNOWN_CLEAR}.
     * @return {@code true} if {@link FieldType} is unknown and not {@link #UNKNOWN_MINE} or {@link #UNKNOWN_CLEAR}, {@code false} otherwise
     */
    public boolean isUnknownNear() {
      return this == UNKNOWN_NEAR_1 || this == UNKNOWN_NEAR_2 || this == UNKNOWN_NEAR_3 || this == UNKNOWN_NEAR_4
              || this == UNKNOWN_NEAR_5 || this == UNKNOWN_NEAR_6 || this == UNKNOWN_NEAR_7 || this == UNKNOWN_NEAR_8;
    }

    /**
     * Checks if {@link FieldType} is unknown.
     * @return {@code true} if {@link FieldType} is unknown, {@code false} otherwise
     */
    public boolean isUnknown() {
      return isUnknownNotMine() || this == UNKNOWN_MINE;
    }

    /**
     * Checks if {@link FieldType} is known.
     * @return {@code true} if known, {@code false} otherwise
     */
    public boolean isKnown() {
      return this == CLEAR || this == NEAR_1 || this == NEAR_2 || this == NEAR_3 || this == NEAR_4 || this == NEAR_5
              || this == NEAR_6 || this == NEAR_7 || this == NEAR_8 || this == MINE;
    }

    /**
     * Checks if {@link FieldType} is flagged.
     * @return {@code true} if flagged, {@code false} otherwise
     */
    public boolean isFlagged() {
      return this == UNKNOWN_FLAG_CLEAR || this == UNKNOWN_FLAG_NEAR_1 || this == UNKNOWN_FLAG_NEAR_2
              || this == UNKNOWN_FLAG_NEAR_3 || this == UNKNOWN_FLAG_NEAR_4 || this == UNKNOWN_FLAG_NEAR_5
              || this == UNKNOWN_FLAG_NEAR_6 || this == UNKNOWN_FLAG_NEAR_7 || this == UNKNOWN_FLAG_NEAR_8
              || this == UNKNOWN_FLAG_MINE;
    }

    /**
     * Gets next type of {@link FieldType}.
     * <br/>
     * {@link #UNKNOWN_CLEAR} will become {@link #UNKNOWN_NEAR_1}, {@link #UNKNOWN_NEAR_1} {@link #UNKNOWN_NEAR_2}, ...,
     * {@link #UNKNOWN_NEAR_7} {@link #UNKNOWN_NEAR_8} and others will become {@link #INVALID}.
     * @return next type of {@link FieldType}
     */
    public FieldType unknownNextType() {
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

    /**
     * Gets cleared type of unknown {@link FieldType}.
     * @return cleared type of unknown {@link FieldType}
     */
    public FieldType unknown2Clear() {
      switch (this) {
        case UNKNOWN_CLEAR:  return CLEAR;
        case UNKNOWN_NEAR_1: return NEAR_1;
        case UNKNOWN_NEAR_2: return NEAR_2;
        case UNKNOWN_NEAR_3: return NEAR_3;
        case UNKNOWN_NEAR_4: return NEAR_4;
        case UNKNOWN_NEAR_5: return NEAR_5;
        case UNKNOWN_NEAR_6: return NEAR_6;
        case UNKNOWN_NEAR_7: return NEAR_7;
        case UNKNOWN_NEAR_8: return NEAR_8;
        case UNKNOWN_MINE:   return MINE;
        default: return INVALID;
      }
    }

    /**
     * Gets flagged type of unknown {@link FieldType}.
     * @return flagged type of unknown {@link FieldType}
     */
    public FieldType unknown2Flagged() {
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

    /**
     * Gets un-flagged type of flagged {@link FieldType}.
     * @return un-flagged type of flagged {@link FieldType}
     */
    public FieldType flagged2Unknown() {
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

    @Override
    public String toString() {
      return this.s;
    }
  }

  /**
   * Possible states of {@link GameGrid}.
   */
  public enum State {
    READY,
    PLAYING,
    FINISHED_MINE,
    FINISHED_WIN,
    FINISHED_OTHER
  }
}
