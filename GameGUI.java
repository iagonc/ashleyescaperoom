import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;

import java.io.File;
import javax.imageio.ImageIO;

import java.util.Random;


public class GameGUI extends JComponent
{
  static final long serialVersionUID = 141L;

  private static final int WIDTH = 510;
  private static final int HEIGHT = 360;
  private static final int SPACE_SIZE = 60;
  private static final int GRID_W = 8;
  private static final int GRID_H = 5;
  private static final int START_LOC_X = 15;
  private static final int START_LOC_Y = 15;
  

  int x = START_LOC_X; 
  int y = START_LOC_Y;


  private Image bgImage;


  private Image player;
  private Point playerLoc;
  private int playerSteps;


  private int totalWalls;
  private Rectangle[] walls; 
  private Image prizeImage;
  private int totalPremios;
  private Rectangle[] prizes;
  private int totalArmadilha;
  private Rectangle[] armadilha;


  private int prizeVal = 10;
  private int trapVal = 5;
  private int endVal = 10;
  private int offGridVal = 5; 
  private int hitWallVal = 5;  


  private JFrame frame;


  public GameGUI()
  {
    
    try {
      bgImage = ImageIO.read(new File("grid.png"));      
    } catch (Exception e) {
      System.err.println("Nao pode abrir a pasta grid.png");
    }      
    try {
      prizeImage = ImageIO.read(new File("coin.png"));      
    } catch (Exception e) {
      System.err.println("Nao pode abrir a pasta coin.png");
    }
  

    try {
      player = ImageIO.read(new File("player.png"));      
    } catch (Exception e) {
     System.err.println("Nao pode abrir a pasta player.png");
    }

    playerLoc = new Point(x,y);

 
    frame = new JFrame();
    frame.setTitle("AshleyEscapeRoom");
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    frame.setVisible(true);
    frame.setResizable(false); 

    totalWalls = 20;
    totalPremios = 3;
    totalArmadilha = 5;
  }


  public void createBoard()
  {
    armadilha = new Rectangle[totalArmadilha];
    createArmadilha();
    
    prizes = new Rectangle[totalPremios];
    createPrizes();

    walls = new Rectangle[totalWalls];
    createWalls();
  }

 
  public int movePlayer(int incrx, int incry)
  {
      int newX = x + incrx;
      int newY = y + incry;
      

      playerSteps++;


      if ( (newX < 0 || newX > WIDTH-SPACE_SIZE) || (newY < 0 || newY > HEIGHT-SPACE_SIZE) )
      {
        System.out.println ("FORA DO LABIRINTO!!");
        return -offGridVal;
      }


      for (Rectangle r: walls)
      {

        int startX =  (int)r.getX();
        int endX  =  (int)r.getX() + (int)r.getWidth();
        int startY =  (int)r.getY();
        int endY = (int) r.getY() + (int)r.getHeight();


        if ((incrx > 0) && (x <= startX) && (startX <= newX) && (y >= startY) && (y <= endY))
        {
          System.out.println("UMA PAREDE NO CAMINHO");
          return -hitWallVal;
        }
 
        else if ((incrx < 0) && (x >= startX) && (startX >= newX) && (y >= startY) && (y <= endY))
        {
          System.out.println("UMA PAREDE NO CAMINHO");
          return -hitWallVal;
        }

        else if ((incry > 0) && (y <= startY && startY <= newY && x >= startX && x <= endX))
        {
          System.out.println("UMA PAREDE NO CAMINHO");
          return -hitWallVal;
        }
 
        else if ((incry < 0) && (y >= startY) && (startY >= newY) && (x >= startX) && (x <= endX))
        {
          System.out.println("UMA PAREDE NO CAMINHO");
          return -hitWallVal;
        }     
      }

      x += incrx;
      y += incry;
      repaint();   
      return 0;   
  }


  public boolean isTrap(int newx, int newy)
  {
    double px = playerLoc.getX() + newx;
    double py = playerLoc.getY() + newy;


    for (Rectangle r: armadilha)
    {

      if (r.getWidth() > 0)
      {
 
        if (r.contains(px, py))
        {
          System.out.println("UMA ARMADILHA A FRENTE");
          return true;
        }
      }
    }

    return false;
  }


  public int springTrap(int newx, int newy)
  {
    double px = playerLoc.getX() + newx;
    double py = playerLoc.getY() + newy;


    for (Rectangle r: armadilha)
    {
      
      if (r.contains(px, py))
      {

        if (r.getWidth() > 0)
        {
          r.setSize(0,0);
          System.out.println("ARMADILHA ACIONADA!");
          return trapVal;
        }
      }
    }
 
    System.out.println("NÃO TEM ARMADILHA AQUI");
    return -trapVal;
  }


  public int pickupPrize()
  {
    double px = playerLoc.getX();
    double py = playerLoc.getY();

    for (Rectangle p: prizes)
    {

      if (p.getWidth() > 0 && p.contains(px, py))
      {
        System.out.println("VOCE PEGOU UM PREMIO!");
        p.setSize(0,0);
        repaint();
        return prizeVal;
      }
    }
    System.out.println("OOPS, SEM PREMIOS AQUI");
    return -prizeVal;  
  }

  public int getSteps()
  {
    return playerSteps;
  }
  

  public void setPrizes(int p) 
  {
    totalPremios = p;
  }
  

  public void setArmadilha(int t) 
  {
    totalArmadilha = t;
  }
  
 
  public void setWalls(int w) 
  {
    totalWalls = w;
  }

 
  public int replay()
  {

    int win = playerAtEnd();
  

    for (Rectangle p: prizes)
      p.setSize(SPACE_SIZE/3, SPACE_SIZE/3);
    for (Rectangle t: armadilha)
      t.setSize(SPACE_SIZE/3, SPACE_SIZE/3);

 
    x = START_LOC_X;
    y = START_LOC_Y;
    playerSteps = 0;
    repaint();
    return win;
  }

 
  public int endGame() 
  {
    int win = playerAtEnd();
  
    setVisible(false);
    frame.dispose();
    return win;
  }


  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;


    g.drawImage(bgImage, 0, 0, null);


    for (Rectangle t : armadilha)
    {
      g2.setPaint(Color.WHITE); 
      g2.fill(t);
    }


    for (Rectangle p : prizes)
    {

      if (p.getWidth() > 0) 
      {
      int px = (int)p.getX();
      int py = (int)p.getY();
      g.drawImage(prizeImage, px, py, null);
      }
    }


    for (Rectangle r : walls) 
    {
      g2.setPaint(Color.BLACK);
      g2.fill(r);
    }
   

    g.drawImage(player, x, y, 40,40, null);
    playerLoc.setLocation(x,y);
  }


  private void createPrizes()
  {
    int s = SPACE_SIZE; 
    Random rand = new Random();
     for (int numPrizes = 0; numPrizes < totalPremios; numPrizes++)
     {
      int h = rand.nextInt(GRID_H);
      int w = rand.nextInt(GRID_W);

      Rectangle r;
      r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);
      prizes[numPrizes] = r;
     }
  }


  private void createArmadilha()
  {
    int s = SPACE_SIZE; 
    Random rand = new Random();
     for (int numArmadilha = 0; numArmadilha < totalArmadilha; numArmadilha++)
     {
      int h = rand.nextInt(GRID_H);
      int w = rand.nextInt(GRID_W);

      Rectangle r;
      r = new Rectangle((w*s + 15),(h*s + 15), 15, 15);
      armadilha[numArmadilha] = r;
     }
  }


  private void createWalls()
  {
     int s = SPACE_SIZE; 

     Random rand = new Random();
     for (int numWalls = 0; numWalls < totalWalls; numWalls++)
     {
      int h = rand.nextInt(GRID_H);
      int w = rand.nextInt(GRID_W);

      Rectangle r;
       if (rand.nextInt(2) == 0) 
       {

         r = new Rectangle((w*s + s - 5),h*s, 8,s);
       }
       else
       {

         r = new Rectangle(w*s,(h*s + s - 5), s, 8);
       }
       walls[numWalls] = r;
     }
  }

 
  private int playerAtEnd() 
  {
    int score;

    double px = playerLoc.getX();
    if (px > (WIDTH - 2*SPACE_SIZE))
    {
      System.out.println("VOCE CONSEGUIU!!");
      score = endVal;
    }
    else
    {
      System.out.println("OOPS, VOCE SAIU MUITO CEDO!");
      score = -endVal;
    }
    return score;
  
  }
}