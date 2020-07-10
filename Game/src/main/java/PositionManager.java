import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * 
 * odpowiedzialna za obsluge pozycji przez klawiature
 */
public class PositionManager implements KeyListener {
/**
 * przyciski
 */
	public boolean up, down, left, right, pause;
	/**
	 * zmienna pomocnicza do sluchania klawiatury
	 */
	private boolean[] keys;
	/**
	 * zmienna pomocnicza przy koordynacji ruchu statku
	 */
	private boolean falling;
	/**
	 * grawitacja
	 */
	private static double gravity;
	
	public PositionManager() {
		keys = new boolean[256];
	}
/**
 * aktulizacja grawitacji
 * @param level aktualny level
 */
	static void updateGravity(int level) {
		try {
			gravity =(float)FileManager.toIntArray(FileManager.readingFromFile("gravity"+level))[0]/1000000;
		} catch (FileNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * update klawiszy wcisnietych
	 */
	public void update() {
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];		
		pause = keys[KeyEvent.VK_K];
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	/**
	 * informacja o nacisnietym klawiszu
	 */
	public void keyPressed(KeyEvent e) {
	keys[e.getKeyCode()] = true;
	}

	@Override
	/**
	 * informacja o puszczeniu klawiszow
	 */
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;

	}
	/**
	 * upadate pozycji komponentow zgodnie z wcisnietymi klawiszami
	 */
	public void updatePosition(Game game,Assets assets) {
		
		falling=true;
		if(this.up)
			{assets.fuelLeft-=0.012f;assets.speedY-=0.0005f;falling=false;}
		if(this.left)
			{assets.fuelLeft-=0.012f;assets.speedX-=0.0002f;}
		if(this.right)
			{assets.fuelLeft-=0.012f;assets.speedX+=0.0002f;}
		
		
		assets.yShip+=assets.speedY;
		assets.xShip+=assets.speedX;
		assets.currentBoundsY+=assets.speedY;
		assets.currentBoundsX+=assets.speedX;
		game.shipBounds.x=(int)assets.currentBoundsX;
		game.shipBounds.y=(int)assets.currentBoundsY;
		if(falling) assets.speedY+=gravity;
		
        
		
	}
	/**
	 * sprawdzanie kolizji
	 * @param shapeA kształt A
	 * @param shapeB kształt B
	 * @param type typ kolizji
	 */
	public static void testCollisions(Shape shapeA, Shape shapeB,String type,Game game,Assets assets) {
		   Area areaA = new Area(shapeA);
		   Area areaB = new Area(shapeB);
		   areaA.intersect(areaB);


if(assets.fuelLeft<=0) {
	   resetStatus(game,assets);
	   assets.lifesLeft--;
}

if(!areaA.isEmpty()) {
	if(type=="ship-terrain") {
		assets.fuelLeft=0;
		assets.lifesLeft--;
		
				   
		resetStatus(game,assets);
	}
	else if(type=="ship-landing") {
		if(Math.abs(assets.speedX)<0.04&&assets.speedY<0.4) {
		game.roundWon=true;
		}
		else {
			assets.fuelLeft=0;
			assets.lifesLeft--;
		
					   
			resetStatus(game,assets);
		}
	}
			   try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			   
}


}
	/**
	 * sprawdzanie pozostalych zyc gracza
	 */
	public static void checkLifes(Game game,Assets assets) {
		if(assets.lifesLeft==0)
		{
		game.gameState="gameOver";
		try {
			if(game.score>FileManager.readingFromFile("Score1").get(0))
				try {
					FileManager.saveHighScore((int)game.score,game.nickName);
				} catch (IOException e) {

					e.printStackTrace();
				}
		} catch (FileNotFoundException | InterruptedException e) {

			e.printStackTrace();
		}
		   try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * restart pozycji statku
	 */
	public static void resetStatus(Game game,Assets assets ) {
	
		assets.speedY=0;
		assets.speedX=0;
		assets.yShip=30;
		assets.xShip=250;
		assets.currentBoundsY=30;
		assets.currentBoundsX=250;
		game.shipBounds.y=30;
		game.shipBounds.x=250;
		game.score+=assets.fuelLeft;
		assets.fuelLeft=100;
		checkLifes(game,assets);
	}
}
