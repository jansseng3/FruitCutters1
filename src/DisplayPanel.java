import javax.imageio.ImageIO;
import javax.swing.*;
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

    private boolean randomApple;
    private boolean randomBanana;
    private boolean randomPineapple;

    private int randomAppleX;
    private int randomAppleY;
    private int randomBananaX;
    private int randomBananaY;
    private int randomPineappleX;
    private int randomPineappleY;

    // Garbage bag tracking
    private int garbageCount;
    private int[][] garbagePositions; // each row is [x, y] for one bag

    private Timer garbageSpawnTimer;   // fires every 1 second to move bags
    private Timer garbageIncreaseTimer; // fires every 10 seconds to add a bag

    public DisplayPanel() {
        score = 0;
        yellowColor = true;
        gameOver = false;
        knifeX = 0;
        knifeY = 0;

        randomApple = true;
        randomBanana = true;
        randomPineapple = true;

        randomAppleX = (int) (860 * Math.random());
        randomAppleY = (int) (480 * Math.random());
        randomBananaX = (int) (860 * Math.random());
        randomBananaY = (int) (480 * Math.random());
        randomPineappleX = (int) (860 * Math.random());
        randomPineappleY = (int) (480 * Math.random());

        // Start with 1 garbage bag
        garbageCount = 1;
        garbagePositions = new int[20][2]; // 20 bags max
        spawnAllGarbage();

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
            pineapple = ImageIO.read(new File("src/pineapple.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            garbage = ImageIO.read(new File("src/garbage.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Every 1 second: move all garbage bags to new random positions
        garbageSpawnTimer = new Timer(1000, e -> {
            if (!gameOver) {
                spawnAllGarbage();
                repaint();
            }
        });
        garbageSpawnTimer.start();

        // Every 10 seconds: add one more garbage bag
        garbageIncreaseTimer = new Timer(10000, e -> {
            if (!gameOver && garbageCount < 20) {
                garbageCount++;
                spawnAllGarbage();
                repaint();
            }
        });
        garbageIncreaseTimer.start();

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // Randomizes positions for all active garbage bags
    private void spawnAllGarbage() {
        for (int i = 0; i < garbageCount; i++) {
            garbagePositions[i][0] = (int) (860 * Math.random()); // x
            garbagePositions[i][1] = (int) (480 * Math.random()); // y
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 960, 580, null);
        g.drawImage(knife, knifeX, knifeY, 100, 100, null);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(yellowColor ? Color.YELLOW : Color.BLACK);
        g.drawString("Score: " + score, 50, 30);
        g.drawString("Avoid the garbage bags! Cut as many fruits as possible by clicking and dragging your cursor!", 50, 50);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER, YOU LOSE :(", 270, 240);
            g.drawString("Final Score: " + score, 360, 480);
        } else if (gameOver != true) {
            if (randomApple) {
                g.drawImage(apple, randomAppleX, randomAppleY, 100, 100, null);
            }
            if (randomBanana) {
                g.drawImage(banana, randomBananaX, randomBananaY, 100, 100, null);
            }
            if (randomPineapple) {
                g.drawImage(pineapple, randomPineappleX, randomPineappleY, 100, 100, null);
            }
            // Draw all active garbage bags
            for (int i = 0; i < garbageCount; i++) {
                g.drawImage(garbage, garbagePositions[i][0], garbagePositions[i][1], 100, 100, null);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) return; // ignore clicks after game over

        Rectangle knifeRect = knifeRectangle();

        if (randomApple && knifeRect.intersects(appleRectangle())) {
            score++;
            randomAppleX = (int) (860 * Math.random());
            randomAppleY = (int) (480 * Math.random());
            repaint();
        }

        if (randomPineapple && knifeRect.intersects(pineappleRectangle())) {
            score += 3;
            randomPineappleX = (int) (860 * Math.random());
            randomPineappleY = (int) (480 * Math.random());
            repaint();
        }
        // Check each garbage bag for collision
        for (int i = 0; i < garbageCount; i++) {
            Rectangle garbageRect = new Rectangle(garbagePositions[i][0], garbagePositions[i][1], 100, 100);
            if (knifeRect.intersects(garbageRect)) {
                gameOver = true;
                garbageSpawnTimer.stop();
                garbageIncreaseTimer.stop();
                repaint();
                return;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

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
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {
            knifeX -= 5;
            try {
                knife = ImageIO.read(new File("src/knife.png"));
            } catch (IOException error) { }
            repaint();
        }
        if (keyCode == KeyEvent.VK_D) {
            knifeX += 5;
            try {
                knife = ImageIO.read(new File("src/knife.png"));
            } catch (IOException error) { }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    private Rectangle knifeRectangle() {
        return new Rectangle(knifeX, knifeY, 100, 100);
    }

    private Rectangle appleRectangle() {
        return new Rectangle(randomAppleX, randomAppleY, 100, 100);
    }

    private Rectangle pineappleRectangle() {
        return new Rectangle(randomPineappleX, randomPineappleY, 100, 100);
    }
}