import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * 
 *odpowiada za przechowywanie i wczytywanie zasobów
 */
public class Assets {
/**
 * obrazek statku
 */
	public Image ship;
	/**
	 *szerokoxc statku
	 */
	public int widthShip;
/**
 * wysokoœsc statku
 */
	public int heightShip ;
	/**
	 * krawedzie statku
	 */
	protected Rectangle shipBounds;

	/**
	 * czcionka
	 */
	public int font;
	/**
	 * pasek paliwa
	 */
	public Rectangle fuelBar;
	/**
	 * wpolrzedne predkosci
	 */
	public int horizontalSpeedStringX, horizontalSpeedStringY,verticalSpeedStringX,verticalSpeedStringY;
	/**
	 * wspolrzedne napisu paliwa
	 */
	public int fuelStringX,fuelStringY;
	/**
	 * pomocniacza zmienna do liczenia predkosci wertykalnej
	 */
	public float speedY;
	/**
	 * pomocniacza zmienna do liczenia prêdkosci horyzontalnej
	 */
	public float speedX;
	/**
	 * wspolrzedne statku
	 */
	public float xShip,yShip;
	/**
	 * wspólrzedne pomocnicze do aktualizacji przemieszczenia krawedzi statku
	 */
	public float currentBoundsX,currentBoundsY;
	/**
	 * pozostale paliwo
	 */
	public  float fuelLeft;
	/**
	 * pozostale ¿ycia
	 */
	public  int lifesLeft;
	/**
	 * wspolrzedne napisu punktow
	 */
	public float scoreStringX,scoreStringY;
	/**
	 * wspo³rzedne napisów pozostalych zyc
	 */
	public float lifesStringX,lifesStringY;
	/**
	 * inicjalizacja podstawowych parametrow gry
	 */
	Assets(){
		widthShip = 28;
		heightShip = 50;
		lifesLeft=3;
		fuelBar = new Rectangle(550, 26, 200, 19);
		font=12;
		horizontalSpeedStringX=20;
		horizontalSpeedStringY=25;
		verticalSpeedStringX=20;
		verticalSpeedStringY=50;
		fuelStringX=500;
		fuelStringY=38;
		try {
			ship = ImageIO.read(getClass().getResource("SpaceShip.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		shipBounds = new Rectangle(0,0,widthShip,heightShip ); 
		speedY=0;
		speedX=0;
		xShip=250;
		yShip=30;
		currentBoundsX=250;
		currentBoundsY=30;
		fuelLeft=100;
		lifesStringX=20;
		lifesStringY=75;
		scoreStringX=20;
		scoreStringY=100;
}
	
	
	
}