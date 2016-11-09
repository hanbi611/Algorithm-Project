/*  
 * This program simply compares two images of 0/1 with the same dimensions
 * and returns the difference percentage. 
 * Adapted from Steven's source code.
 * 
 */

import java.io.*;
import java.util.*;

public class SimpleComparison1 {

	public static void main(String[] args) throws IOException{
		// 1. Read in two images and turn into 2D array.
		Scanner sc1 = new Scanner(new File(args[0])); 
		Scanner sc2 = new Scanner(new File(args[1])); 

		ArrayList<ArrayList<Integer>> imgArray1 = new ArrayList<ArrayList<Integer>>();	
		ArrayList<ArrayList<Integer>> imgArray2 = new ArrayList<ArrayList<Integer>>();
		
		while (sc1.hasNext()){ // read until end of file
			Scanner sc12 = new Scanner(sc1.nextLine());
			ArrayList<Integer> row = new ArrayList<Integer>();
			while(sc12.hasNext()){ // read one row at a time
				row.add(sc12.nextInt());
			}
			imgArray1.add(row); // once one row is read, add to imgArray and continue reading next row
			sc12.close();
		}	
		
		while (sc2.hasNext()){ // read until end of file
			Scanner sc22 = new Scanner(sc2.nextLine());
			ArrayList<Integer> row = new ArrayList<Integer>();
			while(sc22.hasNext()){ // read one row at a time
				row.add(sc22.nextInt());
			}
			imgArray2.add(row); // once one row is read, add to imgArray and continue reading next row
			sc22.close();
		}
		
		sc1.close(); 
		sc2.close();

		
		// 2. Compare two images
		int height1 = imgArray1.size();
		int width1 = imgArray1.get(0).size();
		int height2 = imgArray2.size();
		int width2 = imgArray2.get(0).size();
		
		long diff = 0;
		
		 for (int y = 0; y < height1; y++) {
		      for (int x = 0; x < width1; x++) {
		        int pix1 = imgArray1.get(y).get(x);
		        int pix2 = imgArray2.get(y).get(x);
		        diff += Math.abs(pix1-pix2);
		      }
		    }
		
		 double n = height1*width1;
		 double p = diff/n;
		 System.out.printf("Diff percent: %f\n",p*100);

	}

		
}
