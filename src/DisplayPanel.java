import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, MouseMotionListener {
    private int score;
    private boolean yellowColor;
    private boolean gameOver;
    private int knifeX;
    private int knifeY;
    private BufferedImage background;
    private BufferedImage knife;
    private BufferedImage apple;
    private BufferedImage banana;
    private BufferedImage pineapple;
    private BufferedImage garbage;

    public DisplayPanel() {
        int randomApple = 0;
        int randomBanana = 0;
        int randomPineapple = 0;
        int randomGarbage = 0;

        score = 0;
        yellowColor = true;
        gameOver = false;
        knifeX = 0;
        knifeY = 0;

        int randomAppleX = 0;
        int randomAppleY = 0;

        try {
            background = ImageIO.read(new File("src/kitchen.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            knife = ImageIO.read(new File("src/knife.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            apple = ImageIO.read(new File("src/apple.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            banana = ImageIO.read(new File("src/banana.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            pineapple = ImageIO.read(new File("src/pineapple.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            garbage = ImageIO.read(new File("src/garbage.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 960,580, null);
        g.drawImage(knife, knifeX, knifeY, 100,100, null);

        // set font and color of text
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (yellowColor) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString("Score: " + score, 50, 30);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            if (score == 10) {
                g.drawString("GAME OVER, YOU WIN!", 350, 240);
            } else {
                g.drawString("GAME OVER, YOU LOSE :(", 350, 240);
            }
        } else if (score < 100){
            int randomApple = (int) (1 + (10 * Math.random()));
            int randomAppleX = (int) (960 * Math.random());
            int randomAppleY = (int) (580 * Math.random());
            g.drawImage(apple, randomAppleX, randomAppleY, 100,100, null);;

            int randomBananaX = (int) (960 * Math.random());
            int randomBananaY = (int) (580 * Math.random());
            g.drawImage(banana, randomBananaX, randomBananaY, 100,100, null);;
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) { } // unimplemented
    // unimplemented because if you move your mouse while clicking, this method isn't
    // called, so mouseReleased is best

    @Override
    public void mousePressed(MouseEvent e) { } // unimplemented

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented

    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented

    @Override
    public void keyTyped(KeyEvent e) { } // unimplemented

    @Override
    public void mouseDragged(MouseEvent e) {
        knifeX = e.getX();
        knifeY = e.getY();
        try {
            knife = ImageIO.read(new File("src/knife.png"));
        } catch (IOException error) { }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {  // A key; VK_A equals 65
            knifeX -= 5;
            try {
                knife = ImageIO.read(new File("src/knife.png"));
            } catch (IOException error) { }
            repaint();
        }
        if (keyCode == KeyEvent.VK_D) {  // D key; VK_D equals 65
            knifeX += 5;
            try {
                knife = ImageIO.read(new File("src/knife.png"));
            } catch (IOException error) { }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }  // unimplemented

}
