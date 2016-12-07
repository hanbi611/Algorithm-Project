import java.util.*;

public class Rotator {
	
	public static void main(String[] args) {
		//TextImage t1 = new TextImage("digits/9_2.txt");
		TextImage t1 = new TextImage("images/square4.txt");
		/*TextImage t2 = new TextImage("digits/4_264.txt");
		t1 = t1.average();
		t2 = t2.average();
		t1 = t1.findBoundingBox();
		t2 = t2.findBoundingBox();
		int rows = (t1.getRows() + t2.getRows())/2;
		int cols = (t1.getCols() + t2.getCols())/2;
		t1 = Resizer.resize(t1, rows, cols);
		t2 = Resizer.resize(t2, rows, cols);
		t1.print();
		System.out.println("---------------------------");
		t2.print();
		System.out.println(t1.compare(t2));
		System.out.println(t1.isAveraged() + " " + t2.isAveraged());*/
		//System.out.println(t1.centerOfMassX() + " " + t1.centerOfMassY());
		t1.print();
		System.out.println("--------------------");
		//t1.contour().print();
		Rotator.rotate(t1, Math.PI/2).print();
	}
	
	//rotates a TextImage counterclockwise by theta
	public static TextImage rotate(TextImage img, double theta) {
		int rows = img.getRows();
		int cols = img.getCols();
		int midx = img.centerOfMassX();
		int midy = img.centerOfMassY();
		List<Integer> xcoords = new ArrayList<>();
		List<Integer> ycoords = new ArrayList<>();
		
		//create list of points of colored pixels
		//points are adjusted to be relative to the center of mass
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				int p = img.getPixel(row, col);
				if(p == 1) {
					xcoords.add(col - midx);
					ycoords.add(row - midy);
				}
			}
		}
		
		// init top, left
		int top = 0; int left = 0; 
		
		//transform x and y
		for(int i = 0; i < xcoords.size(); i++) {
			int x = transformX(xcoords.get(i), ycoords.get(i), theta) + midx;
			int y = transformY(xcoords.get(i), ycoords.get(i), theta) + midy;
			
			//find values of top, left
			if(x < left)
				left = x;
			if(y < top)
				top = y;
			
			xcoords.set(i, x);
			ycoords.set(i, y);
		}
		
		//calculates new image boundaries to account for out of bound errors after rotation
		int len = (int)Math.ceil(Math.sqrt((rows * rows) + (cols * cols)));
		
		//init new array
		int[][] newImg = new int[len][len];
		for(int row = 0; row < len; row++)
			for(int col = 0; col < len; col++)
				newImg[row][col] = 0;
		
		//add in pixels at rotated coordinates
		for(int i = 0; i < xcoords.size(); i++) {
			int row = ycoords.get(i) - top;
			int col = xcoords.get(i) - left;
			newImg[row][col] = 1;
		}
		
		return new TextImage(newImg, img.toString(), img.isAveraged());
	}
	
	private static int transformX(int x, int y, double theta) {
		return (int)Math.round((x * Math.cos(-theta)) - (y * Math.sin(-theta)));
	}
	
	private static int transformY(int x, int y, double theta) {
		return (int)Math.round((x * Math.sin(-theta)) + (y * Math.cos(-theta)));
	}
}
