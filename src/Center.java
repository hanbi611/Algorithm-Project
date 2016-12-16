import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Center {

	
	public static TextImage center(TextImage img){
		//list of x,y colored pixels
		List<Integer> xcoords = new ArrayList<>();
		List<Integer> ycoords = new ArrayList<>();
		
		int rows = img.getRows();
		int columns = img.getCols();
		// the boundaries of the cropped image
		int index = 0;
		
		
		double xCenter = img.centerOfMassX();
		double yCenter = img.centerOfMassY();
		//center of mass coordinates
		
		double yDiff = xCenter-1;
		double xDiff = yCenter-1;
		// find the differences to balance out
		
		
		int xPadding = (int)Math.abs((yDiff)- (rows-(yDiff+2)));
		int yPadding = (int)Math.abs((xDiff)-(columns-(xDiff+2)));
		int [][]newImg = new int [rows+xPadding][columns+yPadding];
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < columns; col++) {
				int p = img.getPixel(row, col);
				if(p == 1) {
					xcoords.add(col);
					ycoords.add(row);
				}
			}
		}
		int[][]txtImg= new int[rows][columns];
			for(int i = 0; i < xcoords.size(); i++) {
				int row1 = ycoords.get(i);
				int col = xcoords.get(i);
				txtImg[row1][col] = 1;
			}
//			System.out.println("xPadding is: " + xPadding);
//			System.out.println("yPadding is: " + yPadding);
//			System.out.println("center point is at: "+ xCenter + " " + yCenter);
//			System.out.println("Xbounds. "+ columns);
//			System.out.println("yBounds: " + rows);
//			System.out.println("xDif is:" +yDiff);
//			System.out.println("yDif is:" + xDiff);
		//here are the amount to add for padding
		//if the top is less than bottom, i will now add 2 on the topside.
		if(yDiff>(rows-(yDiff+2))){
			//System.out.println("adding padding below");
			for(int i = 0; i < rows; i++){
				for(int j=0; j < columns; j++){
					newImg[i][j+yPadding]=txtImg[i][j];
					//newImg[i][j] = 9;
				}
			}
			for(int x = rows; x< rows+xPadding; x++){
				for(int y = 0; y < columns; y++){
					//newImg[x][y]=txtImg[x-yPadding][y];
					newImg[x][y+yPadding] = 0;
				}
			}
				
		}
		//if the top is more than the bottom, add to the bottom.
		//here is the troublemaker part..the first half doenst go down. the bottom half goes down
		if (yDiff<(rows-(yDiff+2))){
			//System.out.println("adding padding above");
			for(int i = xPadding; i < rows+xPadding; i++){
				for(int j = 0; j < columns; j++){
					newImg[i][j+yPadding]=txtImg[i-xPadding][j];					
				}
			}
			//should populate from 0 to xPadding
			for(int x = 0; x < xPadding; x++){
				for(int y = 0; y < columns+yPadding; y++){
					newImg[x][y] = 0;
				}
			}
			//should populate from xPadding onwards
			
			
			// i let padding = 9 to see how the image changes. change back to 0 when done
			
		}
		if(xDiff>(columns-(xDiff+2))){
			//System.out.println("adding padding right");
			for(int i = 0; i<rows;i++){
				for(int j = columns; j < columns+yPadding; j++){
				newImg[i+xPadding][j] =0;
				}
			}
			for(int x = 0 ; x< rows; x++){
				for(int y = yPadding; y<columns+yPadding ;y++ ){
					newImg[x+xPadding][y-yPadding] = txtImg[x][y-yPadding];
				}
			}
			
		}
		if(xDiff<(columns-(xDiff+2))){
			//System.out.println("adding padding left");
			//System.out.println("xCenter-xDiff is: " +( xCenter-xDiff));
			for(int i = 0;  i<rows+xPadding;i++){
				for(int j =0; j <yPadding; j++){
				newImg[i][j] = 0 ;
				}
			}
			for(int x = 0; x<rows;x++){
				for(int y = yPadding; y < columns+yPadding ; y++){
					newImg[x][y] = txtImg[x][y-yPadding];
				}
			}
//			System.out.println(columns);
//			System.out.println(rows);
//			System.out.println(xPadding);
		}
		
		
		
		
		
		//if the ydiff is facing down, then add more upwards.
		
		
		//then this is the new image to return...
		return new TextImage(newImg, newImg.toString());
		
		
	}
	public static void main(String[] args) {
		//digits/9_2.txt this one has problems with above.
		//digits/0_100.txt // adding rightside padding works. above doesnt.
		//digits/2_750.txt bottom and left work fine
		//digits/2_76.txt only have add padding to right side
		//digits/4_509.txt
		TextImage t1 = new TextImage("digits/4_509.txt"); // the original test textimage
		TextImage t2 = t1.crop(); //make the second image to print
		t1.print();
		System.out.println("----------");
		t1.crop().print();
		System.out.println("--------------------");
		Center.center(t2).print();
		
	}
}
