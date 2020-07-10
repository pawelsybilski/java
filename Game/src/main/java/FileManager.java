import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class FileManager {

	/**
	 * punkty do rysowania mapy
	 */
	public static List<Integer> vectorXPointsMap,vectorXPointsLanding,vectorYPointsMap,vectorYPointsLanding;
	
	
	/**
	 * wczytywanie z pliku typu Integer
	 * @param coordinates koordynaty szukanego parametru
	 * @return szukany parametr
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public static List <Integer> readingFromFile(String coordinates) throws InterruptedException, FileNotFoundException {//wczytywanie z pliku konfiguracyjnego
		
		File file = new File("src/main/resources/plik_konfiguracyjny.txt");
		String endline_word="0";

		Vector<Integer> wantedVariable;
		wantedVariable = new Vector<Integer>();


	    Scanner sc = new Scanner(file).useDelimiter("\\s");
	    while(!sc.next().equals(coordinates)) {}
	    	sc.next();
	       
	    while ((sc.hasNext())&&(!endline_word.equals("0000"))) {
	        endline_word = sc.next();
	        wantedVariable.add(Integer.parseInt(endline_word));
	    } 
        wantedVariable.remove( wantedVariable.size() - 1 );
        sc.close();

        return wantedVariable;
	}
	
	/**
	 * wczytywanie z pliku typu String 
	 * @param coordinates koordynaty na szukany wyraz
	 * @return szukany wyraz
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public static String readingFromFileString(String coordinates) throws InterruptedException, FileNotFoundException {//wczytywanie z pliku konfiguracyjnego
		
		File file = new File("src/main/resources/plik_konfiguracyjny.txt");
		String endline_word="0";
		String word = null; 

	    Scanner sc = new Scanner(file).useDelimiter("\\s");
	    while(!sc.next().equals(coordinates)) {}
	    sc.next();
	    endline_word = sc.next();
	    word = endline_word;
	    sc.close();

	        return word;
	}
/**
 * zamienia liste Integerow na tablice typu int
 * @param list lista typu Integer
 * @return tablica typu int
 */
	public static int[] toIntArray(List<Integer> list){
		  int[] arrayInt = new int[list.size()];
		  for(int i = 0;i < arrayInt.length;i++)
			  arrayInt[i] = list.get(i);
		  return arrayInt;
		}
/**
 * pobiera mape
 * @param mapNumber numer mapy
 * @return mapa 
 */
	public static Polygon[] getMap(int mapNumber)  {

		try {
		vectorXPointsMap= new ArrayList<Integer>(readingFromFile("xMap"+mapNumber));
		vectorYPointsMap = new ArrayList<Integer>(readingFromFile("yMap"+mapNumber));
		vectorXPointsLanding = new ArrayList<Integer>(readingFromFile("xLanding"+mapNumber));
		vectorYPointsLanding= new ArrayList<Integer>(readingFromFile("yLanding"+mapNumber));
	} catch (FileNotFoundException | InterruptedException e) {
		e.printStackTrace();
	}

		Polygon[] poly = new Polygon[2];
		poly[0]= new Polygon(toIntArray(vectorXPointsMap), toIntArray(vectorYPointsMap), vectorYPointsMap.size());
		poly[1] = new Polygon(toIntArray(vectorXPointsLanding ), toIntArray(vectorYPointsLanding), vectorYPointsLanding.size());

		return poly;
	}
	
	/**
	 * pobierz najwyzszy wynik z pliku
	 * @return najlepszy wynik
	 */
	public static String getHighScores() {
		
		String nameHS1 = null;
		List<Integer> score1=new ArrayList<Integer>();
		try {
			nameHS1=readingFromFileString("Name1");
			score1=readingFromFile("Score1");
		} catch (FileNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}		
		String stringForlabelHS="<html>Highiest Score<br/>"+nameHS1+"<br/>"+score1.get(0)+"</html>";
		
		return stringForlabelHS;
	}
	/**
	 * zapis do pliku najwyzszego wyniku
	 * @param score wynik
	 * @param name nick gracza
	 * @throws IOException
	 */
	public static void saveHighScore(int score,String name) throws IOException {
		
		String k = "Name1";
		int counter=0;
		
		File file = new File("plik_konfiguracyjny.txt");
		Scanner sc = new Scanner(file);
		while(!sc.nextLine().equals(k)) {counter++;}
		counter++;
		sc.close();
		
		String line;
		List<String> lines = new ArrayList<String>();
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);  
		line=br.readLine();
		
		while(line!=null) {
			lines.add(line);
			line=br.readLine();
		}
		lines.set(counter, name+" 0000");
		lines.set(counter+2,Integer.toString(score)+" 0000");
		
		
		FileWriter writer = new FileWriter("plik_konfiguracyjny.txt"); 
		for(String str: lines) {
			writer.write(str+System.lineSeparator());
		}
		writer.close();	
	}


	
}
