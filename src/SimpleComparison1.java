/* Author: Hanbi Kim
 * 
 * This program simply compares two images of 0/1 with same dimension
 * and returns the difference percentage. 
 * Adapted from Steven's source code.
 * 
 */

import java.io.*;
import java.util.*;

public class SimpleComparison1 {

	public static void main(String[] args) throws IOException{
		// 1. Read in two images and turn into 2D array.
		Scanner sc1 = new Scanner(new File(args[0])); //.useDelimiter("\\s"); // read image 1 from command line
		Scanner sc2 = new Scanner(new File(args[1])); //.useDelimiter("\\s"); // read image 2 from command line

		ArrayList<ArrayList<Integer>> imgArray1 = new ArrayList<ArrayList<Integer>>();	
		ArrayList<ArrayList<Integer>> imgArray2 = new ArrayList<ArrayList<Integer>>();
		
		while (sc1.hasNext()){ // read until end of file
			ArrayList<Integer> row = new ArrayList<Integer>();
			boolean rowdone = false;
			while(true){ // read one row at a time
				String pix = sc1.next();
				if (pix.equals("\n"))
					break;
				int pixel = Integer.parseInt(sc1.next());
				row.add(pixel);
				//String next = sc1.next(); // to consume space in between
				
			}
			imgArray1.add(row); // once one row is read, add to imgArray and continue reading next row
		}	
		while (sc2.hasNext()){ // same as above for second image
			ArrayList<Integer> row = new ArrayList<Integer>();
			boolean rowdone = false;
			while(!rowdone){
				int pix = Integer.parseInt(sc2.next());
				row.add(pix);
				String next = sc2.next(); // to consume space in between
				if (next.equals("\n"))
					rowdone = true;
			}
			imgArray2.add(row);
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
		 System.out.printf("Diff percent: %d\n",p*100);

	}

}
