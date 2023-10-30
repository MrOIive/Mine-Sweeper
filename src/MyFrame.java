import javax.swing.*;
import java.awt.event.*;
import java.awt.Point;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements KeyListener {

  MyPanel panel;
  final int MINE = 9;
  final int NOTHING = 0;
  final int FILLED = 1;
  final int NFILLED = 0;
  final int FLAGGED = 2;
  
  MyFrame (){
    panel = new MyPanel();
    panel.makePanel();

    this.setTitle("Mine Sweeper");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(450,450);
    this.setLocationRelativeTo(null);
    this.setIconImage(new ImageIcon("icon.png").getImage());
    this.addKeyListener(this);
    this.setResizable(false);
    this.add(panel);
    this.pack();
    this.setVisible(true);
  }
   @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyReleased(KeyEvent e) {}
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE && !Main.hitMine) {
      int mousX = -1;
      int mousY = -1;
      try {
    	mousX = (int) this.getMousePosition().getX() - 10;
    	mousY = (int) this.getMousePosition().getY() - 30;
      } catch (Exception ex) {}
      Point b = new Point(mousX, mousY);
      for (int x = 0; x < 9; x++) {
        for (int y = 0; y < 9; y++) {
          if (MyPanel.imageBounds[x][y].contains(b) && Main.feild[x][y] == FILLED && Main.NFLAGS > 0) {
            MyPanel.imBoard[x][y] = new ImageIcon("flag.png").getImage();
            Main.feild[x][y] = FLAGGED;
            Main.NFLAGS--;
            panel.saveX = x;
            panel.saveY = y;
            MyPanel.flagL.setText("Flags: "+Main.NFLAGS);
            repaint();
          } else if (MyPanel.imageBounds[x][y].contains(b) && Main.feild[x][y] == FLAGGED) {
            MyPanel.imBoard[x][y] = new ImageIcon("Blank.png").getImage();
            Main.feild[x][y] = FILLED;
            Main.NFLAGS++;
            MyPanel.flagL.setText("Flags: "+Main.NFLAGS);
            repaint();
          }
        }
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_R && Main.hitMine) {
      Main.restart = true;
    }
  }
}