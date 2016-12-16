package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import java.util.Timer;
import java.util.TimerTask;

/**
   BreakoutGame.java extends Game.java, the game engine provided with this project. BreakoutGame.java creates all GUI components and their functionalities, and 
   contains all behind-the-scenes elements for the game
   @author William Pfleger
 */

public class BreakoutGame extends Game {

    //instance vars
    private static boolean pause=false, run=false, brickCollision=false;
    private static int ballX=40, ballY=465, speedX=2, speedY=-2, bricksLeft=24;
    private static int lives=3, level=1, score=0;
    private static Rectangle[][] bricks;
    
    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};

    //paddle
    static private Rectangle paddle = new Rectangle(280,470,160,10) {{
	setFill(Color.WHITE);
    }};

    //ball
    static private Circle ball = new Circle(280, 400, 5)  {{
	setFill(Color.GREEN);
    }};

    //current level
    static private Text levelText = new Text(0,17,"Level: " + level){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
    }};

    //number of lives left
    static private Text livesText = new Text(550,17,"Lives: " + lives){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
    }};

    //pause menu
    static private String menuText = "PAUSE MENU \nThe objective is to break all the blocks on the screen.\nUse the arrow keys to control the paddle.\nPress space to access the pause menu.\nEach block starts as blue, then when hit changes to yellow,\nred, then dissapears.\nIf the ball hits the ground (not blocked by the paddle), you\nlose a life.\nIf you lose all three lives, the game ends.\nWhen you destroy all bricks, you advance to the next level.\nThere are three levels and with each level up\nyou gain a life and the ball speed increases.\nIf you complete all three levels, you win";
    static private Text pauseMenu = new Text(25, 160, menuText){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
	setVisible(false);
    }};

    //win screen
    static private Text winText = new Text(200,280, "Congratulations! You win!"){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
	setVisible(false);
    }};

    //game over screen
    static private Text gameOverText = new Text(200,180, "You have run out of lives.\nGame Over!\nPress enter to restart"){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
	setVisible(false);
    }};

    //score
    static private Text scoreText = new Text(275,17, "Score: " + score){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
    }};

    //informs the user of level up
    static private Text levelUpText = new Text(200,280,"Level up!"){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
	setVisible(false);
    }};

    //intro text
    static private Text introText = new Text(200, 280, "Press enter to begin"){{
	setFont(new Font(20));
	setFill(Color.YELLOW);
    }};

    /**
     * Constructs a new test game.
     * @param stage the primary stage
     */
    public BreakoutGame(Stage stage) {
        super(stage, "TestGame", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, paddle, ball, levelText, livesText, pauseMenu, gameOverText, scoreText, winText, levelUpText, introText);
	bricks=createBricks();
	for(int i=0; i<6; i++){
	    for(int j=0; j<4; j++){
		getSceneNodes().getChildren().addAll(bricks[i][j]);
	    }
	}
	getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::handler);
    } // BreakoutGame

    
    /**Creates the Rectangle [][] object contaning the bricks
     *@return bricks the 2D Rectangle array containing the bricks
     */
    public static Rectangle[][] createBricks(){
	bricks = new Rectangle[6][6];
	for(int i=0; i<6; i++){
	    for(int j=0; j<4; j++){
		Rectangle r = new Rectangle();
		r.setFill(Color.BLUE);
		r.setX(5+(106*i));
		r.setY(20+(22*j));
		r.setWidth(103);
		r.setHeight(20);
		bricks[i][j]=r;
	    }//for
	}//for

	return bricks;
    }//createBricks
    

    /**
     *Handles keyboard input other than arrow keys
     *@param event the KeyEvent being handled
     */
    public void handler(KeyEvent event){
	if(event.getCode()==KeyCode.SPACE){
	    if(!pause){
		pause=true;
		pauseMenu.setVisible(true);
	    }
	    else{
		pause=false;
		pauseMenu.setVisible(false);
	    }
	}
	if(event.getCode()==KeyCode.ENTER){
	    if(gameOverText.isVisible()){
		reset();
	    }
	    else{
		run=true;
		introText.setVisible(false);
	    }
	}
    }//handler

    /**
     *Checks for ball collision with the paddle
     *@return the boolean value representing a collision or not
     */
    public static boolean paddleCollision(){
	if(ball.getBoundsInParent().intersects(paddle.getBoundsInParent()))
	    return true;
	else
	    return false;
    }//paddleCollision

    /**
     *Informs the user they lost when they runs out of lives 
     */
    public static void gameOver(){
	gameOverText.setVisible(true);
	run=false;
    }//gameOver

    /**
     *Resets the game after a game over
     */
    public static void reset(){
	gameOverText.setVisible(false);
	score=0;
	lives=3;
	level=1;
	speedY*=-1;
	ball.setTranslateY(ball.getTranslateY()+speedY);
	levelText.setText("Level: " +level);
	scoreText.setText("Score: " +score);
	livesText.setText("Lives: " +lives);
	for(int i=0; i<6; i++){
	    for(int j=0; j<4; j++){
		bricks[i][j].setVisible(true);
		bricks[i][j].setFill(Color.BLUE);
	    }
	}
	run=true;
    }//reset

    /**
     *Called when the user levels up. If there are levels left to be played, this method resets the bricks, updates the level, and gives the user another life
     */
    public static void levelUp(){
	if(level<3){
	    level++;
	    lives++;
	    levelText.setText("Level: " +level);
	    for(int i=0; i<6; i++){
		for(int j=0; j<4; j++){
		    bricks[i][j].setVisible(true);
		    bricks[i][j].setFill(Color.BLUE);
   		}
	    }
	    //pauses the game momentarilty to display "level up"
	    levelUpText.setVisible(true);
	    run=false;
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
		    public void run(){
			levelUpText.setVisible(false);
			run=true;
			timer.cancel();
		    }
		},
		1500);
	}
	else{
	    run=false;
	    winText.setVisible(true);
	}
    }//levelUp


    /**
     *Update is called at a rate of 60 FPS. It checks for ball collisions with the walls, paddle, and bricks and moves the ball accordingly
     *@param game the current instance of Game, gameTime the current game time
     */
    @Override
    public void update(Game game, GameTime gameTime) {
	//assigns proper ball speed based on current level
	switch(level){
	case 1:
	    if(speedX>0)
		speedX=1;
	    else
		speedX=-1;
	    if(speedY>0)
		speedY=1;
	    else
		speedY=-1;
	    break;
	case 2:
	    if(speedX>0)
		speedX=2;
	    else
		speedX=-2;
	    if(speedY>0)
		speedY=2;
	    else
		speedY=-2;
	    break;
	case 3:
	    if(speedX>0)
		speedX=3;
	    else
		speedX=-3;
	    if(speedY>0)
		speedY=3;
	    else
		speedY=-3;
	    break;
	}
	if(!pause && run){
	    //controls for paddle
	    if (game.getKeyManager().isKeyPressed(KeyCode.LEFT)) 
		paddle.setTranslateX(paddle.getTranslateX() - 5);
	    if (game.getKeyManager().isKeyPressed(KeyCode.RIGHT))
		paddle.setTranslateX(paddle.getTranslateX() + 5);
	    brickCollision=false;
	    //checks for ball collisions with bricks and moves ball accordingly
	    for(int i=0; i<6; i++){
		for(int j=0; j<4; j++){
		    if(bricks[i][j].getBoundsInParent().intersects(ball.getBoundsInParent()) && bricks[i][j].isVisible()){
			if(bricks[i][j].getFill()==Color.BLUE)
			    bricks[i][j].setFill(Color.YELLOW);
			else if(bricks[i][j].getFill()==Color.YELLOW)
			    bricks[i][j].setFill(Color.RED);
			else if(bricks[i][j].getFill()==Color.RED){
			    bricks[i][j].setVisible(false);
			    score++;
			    scoreText.setText("Score: " + score);
			    bricksLeft--;
			    if(bricksLeft==0)
				levelUp();
			}
			speedY*=-1;
			ball.setTranslateY(ball.getTranslateY()+speedY);
		    }
		}
	    }
	    //moves ball in free space (no paddle/brick collisions)
	    if(!paddleCollision() && !brickCollision){
		//move the ball in x direction
		if((ball.getTranslateX()+speedX<=-275 || ball.getTranslateX()+speedX>=360)){
		    speedX*=-1;
		}
		else{
		    ball.setTranslateX(ball.getTranslateX()+speedX);
		}
		//move the ball in y direction
		if(ball.getTranslateY()+speedY<=-400){
		    speedY*=-1;
		}
		//checks for collision with bottom of screen and decrements lives accordingly
		if(ball.getTranslateY()+speedY>=80){
		    if(lives==1){
			livesText.setText("Lives: 0");
			gameOver();
		    }
		    else{
			lives--;
			livesText.setText("Lives: " + lives);
			speedY*=-1;
			ball.setTranslateY(ball.getTranslateY()-1);
		    }
		}    
		else{
		    ball.setTranslateY(ball.getTranslateY()+speedY);
		}
	    }
	    else if(paddleCollision()){
		speedY*=-1;
		ball.setTranslateY(ball.getTranslateY()+speedY);
	    }
	    else if(brickCollision){
		brickCollision=false;
	    }
	    
	}
    } // update

} // BreakoutGame

