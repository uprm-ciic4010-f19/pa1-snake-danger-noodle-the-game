package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Game.Entities.Static.Apple;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int key;
	public int lenght;
	public boolean justAte;
	private Handler handler;

	public double score;


	public int xCoord;
	public int yCoord;

	public int moveCounter;

	public String direction;//is your first name one?

	public int speed = 3;
	public int steps;

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		score = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		lenght= 1;
		steps = 0;

	}
	public boolean getisGood() {
		return Apple.isGood;
	}


	public void tick(){
		moveCounter++;
		steps++;
		if(steps >= 100) {
			Apple.isGood = false;
			if(steps >=350) {
				Apple.isGood = true;
				steps = 0;
			}

		}
		if(moveCounter>=5) {
			checkCollisionAndMove();
			moveCounter = speed;
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
			if(direction !="Down")
				direction="Up";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
			if(direction !="Up")
				direction="Down";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
			if(direction !="Right")
				direction="Left";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
			if(direction !="Left")
				direction="Right";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
			lenght = lenght +1;
			handler.getWorld().body.addFirst(new Tail(xCoord, yCoord, handler));
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			speed ++;
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			speed --;
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			State.setState(handler.getGame().pauseState);
		}

	}




	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				xCoord=handler.getWorld().GridWidthHeightPixelCount-1;
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				xCoord=0;

			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				yCoord=handler.getWorld().GridWidthHeightPixelCount-1;


			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				yCoord=0;
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			Eat();
			score++;
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}
		if(!handler.getWorld().body.isEmpty()) {
			for (key = 0; key < handler.getWorld().body.size(); key ++)
				if( xCoord == handler.getWorld().body.get(key).x && yCoord == handler.getWorld().body.get(key).y) {
					kill();
				}
		}

	if(handler.getWorld().appleLocation[xCoord][yCoord]){
		Eat();
		if(Apple.isGood) {
			score = score + Math.sqrt((2*score)+1);
		}
		else {
			score =score - Math.sqrt(2*score+1);
		}
	}

	if(!handler.getWorld().body.isEmpty()) {
		handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
		handler.getWorld().body.removeLast();
		handler.getWorld().body.addFirst(new Tail(x, y,handler));
	}
	}


	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.GREEN);

				if(playeLocation[i][j]){
					g.setColor(Color.GREEN);
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
				else if (handler.getWorld().appleLocation[i][j]){
					if( Apple.isGood == false) {
						g.setColor(Color.BLACK);
						g.fillRect((i*handler.getWorld().GridPixelsize),
								(j*handler.getWorld().GridPixelsize),
								handler.getWorld().GridPixelsize,
								handler.getWorld().GridPixelsize);
					}else if (Apple.isGood == true) {
						g.setColor(Color.blue);
						g.fillRect((i*handler.getWorld().GridPixelsize),
								(j*handler.getWorld().GridPixelsize),
								handler.getWorld().GridPixelsize,
								handler.getWorld().GridPixelsize);
					}
				}
			}
		}
	}




	public void Eat(){
		lenght++;
		speed +=1;
		Tail tail= null;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		switch (direction){
		case "Left":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		}
		handler.getWorld().body.addLast(tail);
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
	}
	public void rottenAppleEat() {
		if (Apple.isGood = false);
		handler.getWorld().appleLocation[xCoord][yCoord] = false;
		handler.getWorld().appleOnBoard=false;
		handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
		handler.getWorld().body.removeLast();
		}
	
	
	public void kill(){
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

				JFrame window = new JFrame("");
				JOptionPane.showMessageDialog(window, "Game Over");
				System.exit(0);
			}
		}
	}
	
	

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}

	public void highScore (Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial",1,20));
		g.drawString("Score:" +String.valueOf(score),15,675);
	}

}
