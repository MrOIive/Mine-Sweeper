import java.util.Random;

public class Main {
  static int MINE = 9;
  public static boolean hitMine = false;
  public static int NFLAGS = 10;
  static int NOTHING = 0;

  static int FILLED = 1;
  static int NFILLED = 0;
  static int FLAGGED = 2;

  static int bX = 9;
  static int bY = 9;
  public static int[][] board = new int[bX][bY];
  public static int[][] feild = new int[bX][bY];
  
  public static boolean stop = false;
  public static boolean done = false;
  public static boolean restart = true;
  public static boolean firstTouch = true;
    public static int FTX;
    public static int FTY;

  public static int isTouching(int X, int Y) {
    int integer = 0;
    if (X != 0 && Y != 0) {
      if (board[X-1][Y-1] == MINE) {
        integer++;
      }
    }
    if (X != 0) {
      if (board[X-1][Y] == MINE) {
        integer++;
      }
    }
    if (Y != 0) {
      if (board[X][Y-1] == MINE) {
        integer++;
      }
    }
    if (X != 0 && Y != 8) {
      if (board[X-1][Y+1] == MINE) {
        integer++;
      }
    }
    if (Y != 8) {
      if (board[X][Y+1] == MINE) {
        integer++;
      }
    }
    if (X != 8 && Y != 0) {
      if (board[X+1][Y-1] == MINE) {
        integer++;
      }
    }
    if (X != 8) {
      if (board[X+1][Y] == MINE) {
        integer++;
      }
    }   
    if (X != 8 && Y != 8) {
      if (board[X+1][Y+1] == MINE) {
        integer++;
      } 
    }    
    return integer;
  }

  static boolean outOfRange(int x, int y) {
    int xx = FTX;
    int yy = FTY;
    if (xx + 1 < x || yy + 1 < y || xx - 1 > x || yy- 1 > y) {
      return true;
    } else {
      return false;
    }
  }
  
  public static void start() {

    Random rand = new Random();
    
    while (true) {
      System.out.print("");
      if (!firstTouch) {
        break;
      }
    }
    
    int os = 0;
    while (os < 10){
      int rx = rand.nextInt(9);
      int ry = rand.nextInt(9);
      if (board[rx][ry] != MINE && outOfRange(rx, ry)) {
        board[rx][ry] = MINE;
        os++;
      }
    }
     
    for (int i = 0; i < bX; i++) {
      for (int j = 0; j < bY; j++) {
       if (board[i][j] != MINE) {
          board[i][j] = isTouching(i, j);
       }
      }
    }

    long startTime = System.currentTimeMillis();
    done = true;
    
    while (!hitMine) {
      
      stop = true;
      while (true) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long secondsDisplay = elapsedTime / 1000;
        MyPanel.timeL.setText("Timer: "+secondsDisplay);
        if (!stop) {
          break;
        }
      }
      if (MyPanel.nSquares == 10) {
        hitMine = true;
        MyPanel.label.setText("You Win!");
        restart = false;
      }
      if (!restart) {
        while (true) {
          System.out.print("");
          if (restart) {
            break;
          }
        }
        new MyPanel().restartGraphics();
      }
    }
  }
  public static void main(String[] args) {
    new MyFrame();
    start();
  }
}