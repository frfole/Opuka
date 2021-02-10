package ml.frfole.opuka.common.gamegrids;

import java.util.Random;

public abstract class GameGrid {
  private final int height;
  private final int width;
  protected final GameGridFieldType[][] grid;
  protected Random random;
  private int invalidCount = 0;
  private int minesCount = 0;
  private GameGridState state = GameGridState.FINISHED_OTHER;

  public GameGrid(int height, int width) {
    this.height = height;
    this.width = width;
    grid = new GameGridFieldType[height][width];
    prepareGrid();
  }

  /**
   * Sets all values of {@link #grid} to {@code 0}.
   */
  private void prepareGrid() {
    minesCount = 0;
    invalidCount = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        grid[y][x] = GameGridFieldType.UNKNOWN_CLEAR;
      }
    }
    state = GameGridState.READY;
  }

  /**
   * Resets {@link GameGrid}.
   */
  public void reset() {
    state = GameGridState.READY;
    int mc = minesCount;
    minesCount = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (grid[y][x] != GameGridFieldType.INVALID) {
          grid[y][x] = GameGridFieldType.UNKNOWN_CLEAR;
        }
      }
    }
    populateWithMines(mc);
  }

  /**
   * Sets field at x, y as invalid.
   * @param x the x
   * @param y the y
   */
  public void setInvalid(int x, int y) {
    if (!(0 <= x && x < height && 0 <= y && y < width)) return;
    if (grid[y][x] == GameGridFieldType.INVALID) return;
    grid[y][x] = GameGridFieldType.INVALID;
    invalidCount += 1;
  }

  /**
   * Populates {@link #grid} with mines and all fields with corresponding number of mines nearby.
   * @param count the amount of mines
   */
  public void populateWithMines(int count) {
    if (state != GameGridState.READY) return;
    int c = 0;
    if (count > width * height - invalidCount) return;
    while (c < count) {
      int x = this.random.nextInt(width);
      int y = this.random.nextInt(height);
      if (grid[y][x] == GameGridFieldType.UNKNOWN_MINE || grid[y][x] == GameGridFieldType.INVALID) continue;
      minesCount += 1;
      grid[y][x] = GameGridFieldType.UNKNOWN_MINE;
      c += 1;
      for (int cx = x - 1; cx <= x + 1; cx++) {
        for (int cy = y - 1; cy <= y + 1; cy++) {
          if (0 <= cx && cx < width && 0 <= cy && cy < height) {
            if (grid[cy][cx] != GameGridFieldType.UNKNOWN_MINE)
              grid[cy][cx] = grid[cy][cx].unknownNextType();
          }
        }
      }
    }
  }

  /**
   * Dig field at x, and y. If mine is not at given x and y, clear nearby fields.
   * @param x the x
   * @param y the y
   * @return {@code true} if mine at x and y, {@code false} otherwise
   */
  public boolean dig(int x, int y) {
    if (!(state == GameGridState.READY || state == GameGridState.PLAYING) || !(0 <= x && x < width && 0 <= y && y < height)) return false;
    if (grid[y][x].isUnknownNear()) {
      grid[y][x] = grid[y][x].unknownNear2Clear();
    }
    else if (grid[y][x] == GameGridFieldType.MINE) {
      state = GameGridState.FINISHED_MINE;
      return true;
    }
    else if (grid[y][x] == GameGridFieldType.UNKNOWN_CLEAR) {
      grid[y][x] = GameGridFieldType.CLEAR;
      for (int cx = x - 1; cx < x + 2; cx++) {
        for (int cy = y - 1; cy < y + 2; cy++) {
          if (0 <= cx && cx < width && 0 <= cy && cy < height) {
            if (grid[cy][cx].isUnknownNotMine())
              this.dig(cx, cy);
          }
        }
      }
    }
    state = isDone() ? GameGridState.FINISHED_WIN : GameGridState.PLAYING;
    return false;
  }

  /**
   * Mark field at x and y as possible field with mine.
   * @param x the x
   * @param y the y
   */
  public void flag(int x, int y) {
    if (!(0 <= x && x < width && 0 <= y && y < height)) return;
    if (grid[y][x].isUnknown()) {
      grid[y][x] = grid[y][x].unknown2Flagged();
    }
    else if (grid[y][x].isFlagged()) {
      grid[y][x] = grid[y][x].flagged2Unknown();
    }
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
   * Returns count of invalid fields (added using {@link #setInvalid(int, int)}).
   * @return count of invalid fields
   */
  public int getInvalidCount() {
    return invalidCount;
  }

  /**
   * Returns the {@link #grid}.
   * @return the {@link #grid}
   */
  public GameGridFieldType[][] getGrid() {
    return grid;
  }

  /**
   * Checks if is all mines discovered.
   * @return {@code true} if all mines is discovered
   */
  public boolean isDone() {
    int known = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (grid[y][x].isKnown())
          known += 1;
      }
    }
    return width * height - known - invalidCount == minesCount;
  }

  /**
   * Gets current {@link GameGridState} of this {@link GameGrid}.
   * @return current {@link GameGridState}
   */
  public GameGridState getState() {
    return state;
  }

  /**
   * Gets count of mines.
   * @return count of mines
   */
  public int getMinesCount() {
    return minesCount;
  }

}
