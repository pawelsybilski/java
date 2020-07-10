import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * 
 * 	Glowna klasa sterujaca oraz rysujaca gre 
 *
 */
public class Game  extends JPanel implements Runnable,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
 	* Rozmiar okna
 	*/
	private int width;
	private int height;
	/**
	 *level gry 
	 */
	private int level;
	/**
	 *czy wygrano runde 
	 */
	public boolean roundWon;
	/**
	 * Tytul
	 */
	public String title;
	/**
	 * Mapa
	 */
	public Polygon currentMap[];
	/**
	 * krawedzie statku
	 */
	public Rectangle shipBounds;
	/**
	 * czy gra jest uruchomiona
	 */
	private boolean running=false;
	/**
	 * watek
	 */
	private Thread thread;
	/**
	 * obiekt klasy przechowywujacej zasoby
	 */
	private Assets assets;
	/**
	 * obiekt klasy odpowiadajacej za pozycjonowanie komponentow
	 */
	private PositionManager postionManager;
	/**
	 * skala szerokosci
	 */
	private Double widthScaleRatio;
	/**
	 * skala wysokosci
	 */
	private Double heightScaleRatio;
	/**
	 * etykiety do wyswietlania napisow
	 */
	private JLabel label1,label2;
	/**
	 *etykieta do wyswietlania najlepszego wyniku 
	 */
	private JLabel labelHS;
	/**
	 * przycisk startu/powtorki
	 */
	private JButton button;
	/**
	 * pole tekstowe do wpisywania nicku
	 */
	private JTextField nameField; //pole na nazwę
	/**
	 * glowne okno programu
	 */
	public JFrame frame;
	/**
	 * wymiary przycisku
	 */
	private float boundButtonX,boundButtonY,widthButton,heightButton;
	/**
	 * wymiary etykiety
	 */
	private float boundLabel1X,boundLabel1Y,widthLabel1,heightLabel1;
	/**
	 * wymiary etykiety najlepszego wyniku
	 */
	private float boundLabelHSX,boundLabelHSY,widthLabelHS,heightLabelHS;
	/**
	 * wymiary pola tekstowego
	 */
	private float boundNameFieldX,boundNameFieldY,widthNameField,heightNameField;
	/**
	 * wymiary etykiety
	 */
	private float boundLabel2X,heightLabel2,widthLabel2,boundLabel2Y;

	/**
	 * punkty
	 */
	public float score;
	/**
	 * stan gry
	 */
	public String gameState;
	/**
	 * nazwa gracza
	 */
	public String nickName;
	/**
	 * inicjalizuje podstawowe parametry okna
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	public Game() throws FileNotFoundException, InterruptedException{
		width = FileManager.toIntArray(FileManager.readingFromFile("WIDTH"))[0];
		height = FileManager.toIntArray(FileManager.readingFromFile("HEIGHT"))[0];;
		title = FileManager.readingFromFileString("TITLE");
		widthScaleRatio=1.0;
		heightScaleRatio=1.0;
		nickName="NoName";
		gameState="start";
		boundButtonX=300;
		boundButtonY=250;
		widthButton=200;
		heightButton=100;
		boundLabel1X=310;
		boundLabel1Y=150;
		widthLabel1=300;
		heightLabel1=100;
		boundLabelHSX=50;
		boundLabelHSY=50;
		widthLabelHS=300;
		heightLabelHS=400;
		boundNameFieldX=300;
		boundNameFieldY=450;
		widthNameField=200;
		heightNameField=50;
		boundLabel2X=300;
		boundLabel2Y=400;
		widthLabel2=300;
		heightLabel2=50;
		frame = new JFrame(title);
	}
	/**
	 * metoda inicjalizująca parametry poczatkowe gry
	 */
	private void init(){
		level=0;
		roundWon=true;
		score=0;
		postionManager = new PositionManager();
		assets = new Assets(); 
		Display();	
		gameState="start";
		shipBounds = assets.shipBounds;
		currentMap= FileManager.getMap(1);
		PositionManager.updateGravity(1);


	}
	/**
	 * metoda aktualizujaca rozgrywke
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	private void update() throws FileNotFoundException, InterruptedException{

		
		
		if(roundWon) {
			level++;
			switch(level) {
				case 1 : {
					
				currentMap= FileManager.getMap(level);
				shipBounds = assets.shipBounds;
				roundWon=false;
				PositionManager.updateGravity(level);
				break;
				}
				case 2 : {
				PositionManager.resetStatus(this,assets);		
				currentMap= FileManager.getMap(level);
				roundWon=false;
				PositionManager.updateGravity(level);
				break;
				}
				default : {
				PositionManager.resetStatus(this,assets);

				try {
					if(score>FileManager.readingFromFile("Score1").get(0))
						FileManager.saveHighScore((int)score,nickName);
				} catch (IOException e) {

					e.printStackTrace();
				}
					gameState="won";

				break;
				}
			}
		}

		if(gameState=="playing") {
			PositionManager.testCollisions((Shape)currentMap[0],(Shape)shipBounds,"ship-terrain",this,assets);			
			PositionManager.testCollisions((Shape)currentMap[1],(Shape)shipBounds,"ship-landing",this,assets);
			PositionManager.checkLifes(this,assets);
		}
	postionManager.update();
	postionManager.updatePosition(this,assets);
		Thread.sleep(1);

		if(postionManager.pause) 
		{
			Thread.sleep(500);
            while(true){	
                postionManager.update();
                frame.repaint();
                if(postionManager.pause){
                    Thread.sleep(300);	
                    break;
                }
            }
        }
        
		}

	
	
	
/**
 * metoda tworzaca okno
 */
	private void  Display(){
	
			//if(gameState=="start")
			//frame = new JFrame(title);
			
			frame.getContentPane().removeAll();
			button=new JButton("START");
			label1=new JLabel("LUNAR LANDER");
			label2=new JLabel("Enter Nickname");
			nameField=new JTextField();
			labelHS=new JLabel(FileManager.getHighScores());
			
			label1.setForeground(Color.red);
			label2.setForeground(Color.red);
			labelHS.setForeground(Color.red);
			nameField.setForeground(Color.red);
						 	
			frame.add(nameField);
			frame.add(labelHS);
			frame.add(label2);
			button.addActionListener(this); 
			setFrameParameters();
			frame.add(this);
			
			scaleComponents();
	}
	
	/**
	 * metoda reagujaca na kliniecie przycisku, startuje/powtarza rozgrywke
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

if(gameState=="start") {

	if(!nameField.getText().isEmpty())
			
		nickName=nameField.getText();
	    frame.getContentPane().removeAll();
		frame.add(this); 
		gameState="playing";
		frame.addKeyListener(postionManager);
		frame.requestFocus();
	}
	else {

		PositionManager.resetStatus(this,assets);
		button.setName("start");
		init();
	}
}
	/**
	 * metoda rysujaca cała rozgyrwke
	 */
	@Override
	public void paintComponent(Graphics g) {
super.paintComponent(g);	


		Graphics2D bg = (Graphics2D) g;
		
		bg.setColor(Color.BLACK);
		bg.fillRect(0, 0, width, height);
	
		widthScaleRatio=(widthScaleRatio*frame.getWidth())/width;
		heightScaleRatio=(heightScaleRatio*frame.getHeight())/height;

		width=frame.getWidth();
		height=frame.getHeight();
		scaleComponents();
		
switch (gameState) {
	case "playing" :{
		bg.scale(widthScaleRatio,heightScaleRatio);


		bg.drawImage(assets.ship, (int)assets.xShip, (int)assets.yShip, null);
		bg.setColor(Color.RED);
		bg.drawString("Vertical : "+assets.speedY*10+" m/s", assets.verticalSpeedStringX, assets.verticalSpeedStringY);
		bg.drawString("Horizontal : "+assets.speedX*10+" m/s", assets.horizontalSpeedStringX, assets.horizontalSpeedStringY);
		bg.drawString("FUEL", assets.fuelStringX, assets.fuelStringY);
		bg.drawString("Lifes left : "+ assets.lifesLeft, assets.lifesStringX, assets.lifesStringY);
		bg.drawString("Score  : "+ (int)score, assets.scoreStringX, assets.scoreStringY);

		BasicStroke stroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,10);
	    bg.setStroke(stroke);  
		bg.setColor(Color.ORANGE);
		bg.fillRect((int)assets.fuelBar.x, assets.fuelBar.y,(int) (assets.fuelBar.width+(assets.fuelLeft*2)-200), assets.fuelBar.height);
		bg.setColor(Color.GRAY);
		bg.drawRect(assets.fuelBar.x-1, assets.fuelBar.y+1, assets.fuelBar.width, assets.fuelBar.height-1);	

		bg.setColor(Color.gray);
		bg.fillPolygon(currentMap[0]);
		bg.setColor(Color.RED); 
		bg.fillPolygon(currentMap[1]);
	/*
		PositionManager.testCollisions((Shape)currentMap[0],(Shape)shipBounds,"ship-terrain",this);			
		PositionManager.testCollisions((Shape)currentMap[1],(Shape)shipBounds,"ship-landing",this);
		PositionManager.checkLifes(this);*/
		break;
}
	case "won" :{
		button=new JButton("REPLAY");
		label1=new JLabel("LUNAR LANDER");
		
		button.addActionListener(this); 
		setFrameParameters();
		frame.add(this);
		label1.setText("<html> You won !!!<br/>"+" Score : "+(int)score+"</html>");	
		gameState="ending";
		break;

}
	case "gameOver" :{
		button=new JButton("REPLAY");
		label1=new JLabel("LUNAR LANDER");
		button.addActionListener(this); 
		setFrameParameters();
		frame.add(this);
		label1.setText("<html> You Lost !!!<br/>"+" Score : "+(int)score+"</html>");

		gameState="ending";
		assets.lifesLeft=3;
		
		break;
	}

}



}
	/**
	 * ustawia podstawowe parametry okna
	 */
	private void setFrameParameters() {
		

		frame.add(label1);
		frame.add(button);
		frame.pack();
		frame.setSize(width,height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);	
		label1.setForeground(Color.red);

	}
	/**
	 * metoda sklaujaca componenty poza rozgywka
	 */
	public void scaleComponents() {
		nameField.setFont(new Font("Courier New", Font.ITALIC, (int)((widthScaleRatio+heightScaleRatio)/2*assets.font*3)));
		nameField.setBounds((int)(boundNameFieldX*widthScaleRatio),(int)(boundNameFieldY*heightScaleRatio),(int)(widthNameField*widthScaleRatio),(int)(heightNameField*heightScaleRatio));
		button.setFont(new Font("Courier New", Font.ITALIC, (int)((widthScaleRatio+heightScaleRatio)/2*assets.font*3)));
		button.setBounds((int) (boundButtonX*widthScaleRatio),(int)(boundButtonY*heightScaleRatio),(int)(widthButton*widthScaleRatio),(int)(heightButton*heightScaleRatio)); 
		label1.setFont(new Font("Courier New", Font.ITALIC, (int)((widthScaleRatio+heightScaleRatio)/2*assets.font*2)));
		label1.setBounds((int) (boundLabel1X*widthScaleRatio),(int)(boundLabel1Y*heightScaleRatio),(int)(widthLabel1*widthScaleRatio),(int)(heightLabel1*heightScaleRatio)); 
		label2.setFont(new Font("Courier New", Font.ITALIC, (int)((widthScaleRatio+heightScaleRatio)/2*assets.font*2)));
		label2.setBounds((int) (boundLabel2X*widthScaleRatio),(int)(boundLabel2Y*heightScaleRatio),(int)(widthLabel2*widthScaleRatio),(int)(heightLabel2*heightScaleRatio)); 
		labelHS.setFont(new Font("Courier New", Font.ITALIC, (int)((widthScaleRatio+heightScaleRatio)/2*assets.font*2)));
		labelHS.setBounds((int) (boundLabelHSX*widthScaleRatio),(int)(boundLabelHSY*heightScaleRatio),(int)(widthLabelHS*widthScaleRatio),(int)(heightLabelHS*heightScaleRatio)); 
		
	}
	



	/**
	 * metoda watku zawierajaca nieskończoną petle
	 */
	public void run(){
	
			init();

		while(running){
					try {
						if(gameState=="playing")
						update();
						frame.repaint();
					} catch (FileNotFoundException | InterruptedException e) {
						e.printStackTrace();
					}
				
		}
		stop();
	}
	

	/**
	 * startowanie gry
	 */
	public synchronized void start(){
		
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * zatrzymanie gry
	 */
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}



