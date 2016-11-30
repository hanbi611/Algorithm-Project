import java.io.File;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		String input1 = args[0];
		String input2 = args[1];
		//String input3 = args[2];
		
		File inputFolder1 = new File(input1); 
		ArrayList<TextImage> queryImages = new ArrayList<TextImage>();
		File inputFolder2 = new File(input2);
		ArrayList<TextImage> compareImages = new ArrayList<TextImage>();
		
		File outputFolder = new File(args[2]);
		int k = Integer.parseInt(args[3]);

		// Loop through inputfolder 1&2 to process Queryimages
		String[] queryImgNames = inputFolder1.list();
		for (String s : queryImgNames){
			String path = input1 + "/" + s;
			TextImage queryti = new TextImage(path); // image pre-processing happens in the constructor
			queryImages.add(queryti);
		}
		
		String[] compareImgNames = inputFolder2.list();
		for (String s : compareImgNames){
			String path = input2 + "/" + s;
			TextImage comparei = new TextImage(path);
			compareImages.add(comparei);
		}
		

	}



}

