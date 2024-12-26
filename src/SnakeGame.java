import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
            
        }
    }
    int BoardWidth;
    int BoardHeight;
    int TileSize = 25;

    //Snake
    Tile SnakeHead;
    ArrayList<Tile> SnakeBody;

    //Food
    Tile food;
    Random random;

    //Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean GameOver = false;

    SnakeGame(int BoardWidth, int BoardHeight){
        this.BoardWidth = BoardWidth;
        this.BoardHeight = BoardHeight;
        setPreferredSize(new Dimension(this.BoardWidth, this.BoardHeight)); 
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        SnakeHead = new Tile(5,5);
        SnakeBody = new ArrayList<Tile>();
        
        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //*Grid
        //?for (int i = 0; i < BoardWidth/TileSize; i++){
        //?    g.drawLine(i*TileSize, 0, i*TileSize, BoardHeight);
        //?    g.drawLine(0, i*TileSize, BoardWidth, i*TileSize);
        //?}
        
        //Food
        g.setColor(Color.red);
        //*g.fillRect(food.x * TileSize, food.y * TileSize, TileSize, TileSize);
        g.fill3DRect(food.x * TileSize, food.y * TileSize, TileSize, TileSize, true);

        //Snake Head
        g.setColor(Color.green);
        //*g.fillRect(SnakeHead.x * TileSize, SnakeHead.y * TileSize, TileSize, TileSize);
        g.fill3DRect(SnakeHead.x * TileSize, SnakeHead.y * TileSize, TileSize, TileSize, true);

        //Snake Body
        for (int i = 0; i < SnakeBody.size(); i++){
            Tile SnakePart = SnakeBody.get(i);
            //*g.fillRect(SnakePart.x * TileSize, SnakePart.y * TileSize, TileSize, TileSize);
            g.fill3DRect(SnakePart.x * TileSize, SnakePart.y * TileSize, TileSize, TileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (GameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(SnakeBody.size()), TileSize - 16, TileSize);
        } else {
            g.drawString("Score: " + String.valueOf(SnakeBody.size()), TileSize - 16, TileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(BoardWidth/TileSize); 
        food.y = random.nextInt(BoardHeight/TileSize);
    }

    public boolean collision(Tile Tile1, Tile Tile2){
        return Tile1.x == Tile2.x && Tile1.y == Tile2.y;
    }
    
    public void move(){
        //eat food
        if (collision(SnakeHead, food)){
            SnakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        
        //Snake Body
        for (int i = SnakeBody.size()-1; i >= 0; i--){
            Tile SnakePart = SnakeBody.get(i);
            if (i==0){
                SnakePart.x = SnakeHead.x;
                SnakePart.y = SnakeHead.y;
            } else{
                Tile PrevSnakePart = SnakeBody.get(i-1);
                SnakePart.x = PrevSnakePart.x;
                SnakePart.y = PrevSnakePart.y;
            }
        }
        //Snake Head
        SnakeHead.x += velocityX;
        SnakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < SnakeBody.size(); i++){
            Tile SnakePart = SnakeBody.get(i);
            //collide with snake head
            if (collision(SnakeHead, SnakePart)){
                GameOver = true;
            }
        }
        if (SnakeHead.x * TileSize < 0 || SnakeHead.x * TileSize > BoardWidth || 
            SnakeHead.y * TileSize < 0 || SnakeHead.y* TileSize > BoardHeight){
                GameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (GameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }

    //* do not need */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}