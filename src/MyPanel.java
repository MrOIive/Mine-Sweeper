import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class MyPanel extends JPanel implements MouseListener {

  final int PANEL_WIDTH = 450;
  final int PANEL_HEIGHT = 450;
  final int IMAGE_SIZE = 50;
  final int MINE = 9;
  final int NOTHING = 0;
  final int FILLED = 1;
  final int NFILLED = 0;
  final int FLAGGED = 2;
  
  public int saveX;
  public int saveY;
  
  public static Rectangle[][] imageBounds = new Rectangle[9][9];
  public static Image[][] imBoard = new Image[9][9];
  public static OutlineLabel label;
  public static OutlineLabel flagL;
  public static OutlineLabel timeL;
  public static int nSquares = 9 * 9;
  public static boolean mouseHere = false;
    public static int MHposX;
    public static int MHposY;

  public static boolean paintDone = false;

  MyPanel (){
    
  }
  public void makePanel() {
    this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
    this.addMouseListener(this);
    this.setLayout(null);

    label = new OutlineLabel(3);
    label.setFont(new Font("Comic Sans", Font.BOLD, 50));
    label.setBounds(0,0,450,450);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setForeground(Color.WHITE);
    label.setOutlineColor(Color.BLACK);

    timeL = new OutlineLabel("Timer: 0",1);
    timeL.setFont(new Font("Comic Sans", Font.BOLD, 15));
    timeL.setForeground(Color.WHITE);
    timeL.setOutlineColor(Color.BLACK);
    timeL.setBounds(40, 5, 410, 25);

    flagL = new OutlineLabel("Flags: 10",1);
    flagL.setFont(new Font("Comic Sans", Font.BOLD, 15));
    flagL.setForeground(Color.WHITE);
    flagL.setOutlineColor(Color.BLACK);
    flagL.setBounds(325, 5, 125, 25);
    

    this.add(timeL);
    this.add(flagL);
    this.add(label);

    int addx = 0;
    int addy = 0;
    for (int x = 0; x < 9; x++) {
      for (int y = 0; y < 9; y++) {
        imageBounds[x][y] = new Rectangle(addx, addy, IMAGE_SIZE, IMAGE_SIZE);
        imBoard[x][y] = new ImageIcon("Blank.png").getImage();
        Main.feild[x][y] = FILLED;
        addx += IMAGE_SIZE;
      }
      addx = 0;
      addy += IMAGE_SIZE;
    }
  }

  public void restartGraphics() {
    label.setText("");
    for (int x = 0; x < 9; x++) {
      for (int y = 0; y < 9; y++) {
        imBoard[x][y] = new ImageIcon("Blank.png").getImage();
        Main.feild[x][y] = FILLED;
        Main.board[x][y] = 0;
      }
    }
    flagL.setText("Flags: 10");
    timeL.setText("Timer: 0");
    Main.firstTouch = true;
    Main.hitMine = false;
    Main.done = false;
    Main.NFLAGS = 10;
    nSquares = 9 * 9;
    repaint();
    if (paintDone)
      Main.start();
  }

   protected void paintComponent(Graphics g) {
    paintDone = false;
		super.paintComponent(g); 
		Graphics2D g2D = (Graphics2D) g;
    int addx = 0;
    int addy = 0;
    for (int x = 0; x < 9; x++) {
      for (int y = 0; y < 9; y++) {
        if (Main.feild[x][y] == NFILLED && Main.board[x][y] != MINE) {
          imBoard[x][y] = new ImageIcon(Main.board[x][y]+".png").getImage();
        }
        Image image = imBoard[x][y];
        g2D.drawImage(image, addx, addy, IMAGE_SIZE, IMAGE_SIZE, null);
        addx += IMAGE_SIZE;
      }
      addx = 0;
      addy += IMAGE_SIZE;
    }
     paintDone = true;
	}
  
  @Override
  public void mousePressed(MouseEvent e) {
    if (Main.stop || Main.firstTouch) {
      Point point = e.getPoint();
      for (int x = 0; x < 9; x++) {
        for (int y = 0; y < 9; y++) {
          if (imageBounds[x][y].contains(point) && Main.feild[x][y] == FILLED) {
        	saveX = x;
            saveY = y;
            imBoard[x][y] = new ImageIcon("0.png").getImage();
            break;
          }
        }
      }
      repaint();
    }
  }
  @Override
  public void mouseClicked(MouseEvent e) {}
  @Override
  public void mouseReleased(MouseEvent e) {
    if (Main.stop && Main.feild[saveX][saveY] == FILLED) {
    	int x = saveX;
      	int y = saveY;
    	if (Main.board[x][y] == MINE) {
    		label.setText("You Lose!");
    		Main.hitMine = true;
    		for (int i = 0; i < 9; i++) {
    			for (int j = 0; j < 9; j++) {
    				if (Main.feild[i][j] == FILLED) {
    					if (Main.board[i][j] == MINE ) {
    						imBoard[i][j] = new ImageIcon("mineOS.png").getImage();
    					}
    				}
    				if (Main.feild[i][j] == FLAGGED && Main.board[i][j] != MINE) {
    					imBoard[i][j] = new ImageIcon("mineOSX.png").getImage();
    				}
    			}
    		}
    		imBoard[x][y] = new ImageIcon("bomb.png").getImage();
    		repaint();
    		Main.restart = false;
    		Main.stop = false;
    	} else {
    		floodFill(x, y);
    		repaint();
    		Main.stop = false;
    	}
    } else if (Main.firstTouch && Main.feild[saveX][saveY] == FILLED) {
    	Main.FTX = saveX;
    	Main.FTY = saveY;
    	Main.firstTouch = false;
    	while (true) {
    		System.out.print("");
    		if (Main.done) 
    			break;
    	}
    	floodFill(Main.FTX, Main.FTY);
    	repaint();
    }
  }
  @Override
  public void mouseEntered(MouseEvent e) {}
    
  @Override
  public void mouseExited(MouseEvent e) {}

   public void floodFill(int x, int y ) {   
    if (Main.feild[x][y] == FILLED && Main.board[x][y] != MINE) {
      Main.feild[x][y] = NFILLED;
      nSquares--;
      if (Main.board[x][y] == NOTHING) {
        if (x != 0 && y != 0) {
          floodFill(x-1, y-1);
        }
        if (x != 0) {
          floodFill(x-1, y);
        }
        if (y != 0) {
          floodFill(x, y-1);
        }
        if (x != 0 && y != 8) {
          floodFill(x-1, y+1);
        }
        if (y != 8) {
          floodFill(x, y+1);
        }
        if (x != 8 && y != 0) {
          floodFill(x+1, y-1);
        }
        if (x != 8) {
          floodFill(x+1, y);
        }   
        if (x != 8 && y != 8) {
          floodFill(x+1, y+1);
        }
      }
    }
  }
}