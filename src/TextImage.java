import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class TextImage {
	//private List<List<Integer>> img;
	private int[][] img;
	private int rows, cols;
	private String name;
	private boolean averaged = false;
	private final int noiseFactor = 3;

	public TextImage(int[][] img, String name) {
		this.cols = img[0].length;
		this.rows = img.length;
		this.img = img;
		this.name = name;
	}

	public TextImage(int rows, int cols, String name) {
		img = new int[rows][cols];
		this.rows = rows;
		this.cols = cols;
		this.name = name;
	}

	public TextImage(File f) {
		this.name = f.toString();
		int[][] img;
		int cols = 0;
		int rows = 0;
		try {
			Scanner fileScanner = new Scanner(f);
			while (fileScanner.hasNextLine()) {
				rows++;
				Scanner lineScanner = new Scanner(fileScanner.nextLine());
				while (lineScanner.hasNextInt()) {
					cols++;
					lineScanner.nextInt();
				}
				lineScanner.close();
			}
			cols = cols / rows;
			img = new int[rows][cols];
			fileScanner.close();
			fileScanner = new Scanner(f);
			for (int row = 0; row < rows; row++) {
				Scanner lineScanner = new Scanner(fileScanner.nextLine());
				for (int col = 0; col < cols; col++) {
					int n = lineScanner.nextInt();
					img[row][col] = n;
				}
				lineScanner.close();
			}
			fileScanner.close();
			this.img = img;
			this.rows = rows;
			this.cols = cols;
			averaged = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	//	public TextImage(String pathname) {
	//		this.name = pathname;
	//		int[][] img;
	//		int cols = 0;
	//		int rows = 0;
	//		try {
	//			Scanner fileScanner = new Scanner(new File(pathname));
	//			while(fileScanner.hasNextLine()) {
	//				rows++;
	//				Scanner lineScanner = new Scanner(fileScanner.nextLine());
	//				while(lineScanner.hasNextInt()) {
	//					cols++;
	//					lineScanner.nextInt();
	//				}
	//				lineScanner.close();
	//			}
	//			cols = cols/rows;
	//			img = new int[rows][cols];
	//			fileScanner.close();
	//			fileScanner = new Scanner(new File(pathname));
	//			for(int row=0; row<rows; row++) {
	//				Scanner lineScanner = new Scanner(fileScanner.nextLine());
	//				for(int col=0; col<cols; col++) {
	//					int n = lineScanner.nextInt();
	//					img[row][col] = n;
	//				}
	//				lineScanner.close();
	//			}
	//			fileScanner.close();
	//			this.img = img;
	//			this.rows = rows;
	//			this.cols = cols;
	//			averaged = false;
	//		}
	//		catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
	//	
	//	private static int getColor(int x, int y, int color, BufferedImage img) {
	//		int value = img.getRGB(x, y) >> color & 0xff;
	//		return value;
	//	}
	//	
	//	public boolean isBlack(int x, int y, BufferedImage img) {
	//		return getColor(x, y, 16, img) == 0;
	//	}
	//	
	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public int getPixel(int row, int col) {
		return img[row][col];
	}

	public int[][] getImg() {
		return img;
	}

	public boolean setPixel(int p, int row, int col) {
		try {
			img[row][col] = p;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean isAveraged() {
		return averaged;
	}

	private void setAveraged(boolean b){
		averaged = b;
	}

	public void print(){
		for(int row=0; row<rows; row++) {
			for(int col=0; col<cols; col++) {
				System.out.print(getPixel(row, col) + " ");
			}
			System.out.println("");
		}
	}

	/**
	 * This method returns a new image with noise cancelled out. 
	 * If there are less than 3 adjacent pixels,
	 * the pixel will be considered a noise and will be change to 0.
	 * @return Noise Cancelled TextImage
	 */
	public TextImage cancelNoise() {
		int cols = getCols();
		int rows = getRows();
		TextImage avg = new TextImage(rows, cols, toString());
		for(int col=0; col<cols; col++) {
			int[] cs = {col-1, col, col+1};
			for(int row=0; row<rows; row++) {
				int[] rs = {row-1, row, row+1};
				int sum = 0;
				for(int c : cs) {
					if(!((c < 0) || (c >= cols)))
						for(int r : rs) 
							if(!((r < 0) || (r >= rows))) 
								sum += getPixel(r, c);
				}
				if (sum <= noiseFactor)
					avg.setPixel(0,row,col);
			}
		}
		return avg;
	}

	/**
	 * This method calculates the sum of adjacent pixels of all pixels
	 * and reset the pixel with that sum. 
	 * @return averaged TextImage
	 */
	public TextImage average() {
		int cols = getCols();
		int rows = getRows();
		TextImage avg = new TextImage(rows, cols, toString());
		for(int col=0; col<cols; col++) {
			int[] cs = {col-1, col, col+1};
			for(int row=0; row<rows; row++) {
				int[] rs = {row-1, row, row+1};
				int sum = 0;
				for(int c : cs) {
					if(!((c < 0) || (c >= cols)))
						for(int r : rs) 
							if(!((r < 0) || (r >= rows))) 
								sum += getPixel(r, c);
				}
				avg.setPixel(sum, row, col);
			}
		}
		avg.setAveraged(true);
		return avg;
	}

	/**
	 * This method will find the bounding box of an image
	 * and crop to size. 
	 * @return cropped TextImage
	 */
	public TextImage crop(){
		int top = 0;
		int bot = rows-1;
		int left = 0;
		int right = cols-1;

		//find top and bottom borders
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(img[row][col] > 0) {
					if(top == 0)
						top = row;
					bot = row;
				}
			}
		}

		//find left and right borders
		for(int col = 0; col < cols; col++) {
			for(int row = 0; row < rows; row++) {
				if(img[row][col] > 0) {
					if(left == 0)
						left = col;
					right = col;
				}
			}
		}

		//create new TextImage
		int newrow = bot - top + 1;
		int newcol = right - left + 1;
		int[][] newImg = new int[newrow][newcol];
		for(int row = 0; row < newrow; row++) {
			for(int col = 0; col < newcol; col++)
				newImg[row][col] = img[row+top][col+left];
		}
		return new TextImage(newImg, toString());
	}

	/**
	 * This method will take desired dimension as arguments and
	 * resize the image to that dimension. 
	 * @param txtImg
	 * @param newRow
	 * @param newCol
	 * @return resized TextImage
	 */
	public TextImage resize(int newRow, int newCol) {
		int rows = getRows();
		int cols = getCols();
		int rk[] = new int[newRow];
		int ck[] = new int[newCol];

		// rk[i] is the closest row index of the original txtImg for row i in the resized img
		for(int i = 0; i<newRow; i++) {
			double x = ((double)i * rows)/newRow;
			int ik = (int)Math.round(x);
			if(ik == rows) //prevent index error
				ik--;
			rk[i] = ik;
		}

		// ck[j] is the closest col index of the original txtImg for col j in the resized img
		for(int j = 0; j<newCol; j++) {
			double y = ((double)j * cols)/newCol;
			int jk = (int)Math.round(y);
			if(jk == cols) //prevent index error
				jk--;
			ck[j] = jk;
		}
		int img[][] = new int[newRow][newCol];

		//interpolate
		for(int row = 0; row < newRow; row++)
			for(int col = 0; col < newCol; col++)
				img[row][col] = getPixel(rk[row], ck[col]);

		return new TextImage(img, toString()); 
	}

	public int centerOfMassX() {
		int rows = getRows();
		int cols = getCols();
		int area = 0;;
		int sum = 0;
		for(int row = 0; row < rows; row++)
			for(int col = 0; col < cols; col++) {
				int pixel = getPixel(row, col);
				if(pixel == 1) {
					sum = sum + col;
					area++;
				}
			}
		double xc = ((double)sum)/area;
		return (int)Math.round(xc);
	}

	public int centerOfMassY() {
		int rows = getRows();
		int cols = getCols();
		int area = 0;
		int sum = 0;
		for(int row = 0; row < rows; row++)
			for(int col = 0; col < cols; col++) {
				int pixel = getPixel(row, col);
				if(pixel == 1) {
					sum = sum + row;
					area++;
				}
			}
		double yc = ((double)sum)/area;
		return (int)Math.round(yc);
	}

	public TextImage contour() {
		int[][] newImg = new int[rows][cols];
		int currentRow = 0;
		int currentCol = 0;

		//init newImg
		for(int row = 0; row < rows; row++)
			for(int col = 0; col < cols; cols++)
				newImg[row][col] = 0;

		//do row contours
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				int pixel = getPixel(row, col);
				if(pixel != currentRow) { //found edge
					newImg[row][col] = 1;
					System.out.println("found edge");
				}
				currentRow = pixel;
			}
		}

		//do col contours
		for(int col = 0; col < cols; col++) {
			for(int row = 0; row < rows; row++) {
				int pixel = getPixel(row, col);
				if(pixel != currentCol)
					newImg[row][col] = 1;
				currentCol = pixel;
			}
		}

		return new TextImage(newImg, toString());
	}

	public int categorize() {
		int w = getCols();
		int h = getRows();
		int sum = 0;

		//upper left
		for(int x = (w/8); x < (3*w)/8; x++) {
			for(int y = (h/8); y < (3*h)/8; y++) {
				sum = sum + img[y][x];
			}
		}

		//upper right
		for(int x = (5*w/8); x < (7*w)/8; x++) {
			for(int y = (h/8); y < (3*h)/8; y++) {
				sum = sum + img[y][x];
			}
		}

		//lower left
		for(int x = (w/8); x < (3*w)/8; x++) {
			for(int y = (5*h/8); y < (7*h)/8; y++) {
				sum = sum + img[y][x];
			}
		}

		//lower right
		for(int x = (5*w/8); x < (7*w)/8; x++) {
			for(int y = 5*(h/8); y < (7*h)/8; y++) {
				sum = sum + img[y][x];
			}
		}

		double n = sum * 4;
		double frac = 10 * n/(w * h);
		//return some integer between 0 and 9
		return (int)frac;
	}

	@Override
	public String toString() {
		return name;
	}

	public double compare(TextImage t) {
		int r1 = getRows();
		int c1 = getCols();
		int r2 = t.getRows();
		int c2 = t.getCols();

		TextImage resized1 = Resizer.resize(this, (r1 + r2)/2, (c1 + c2)/2);
		TextImage resized2 = Resizer.resize(t, (r1 + r2)/2, (c1 + c2)/2);

		double diff = 0;

		for(int col=0; col<resized1.getCols(); col++) {
			for(int row=0; row<resized1.getRows(); row++) {
				int pix1 = resized1.getPixel(row, col);
				int pix2 = resized2.getPixel(row, col);
				diff += Math.abs(pix1-pix2);
			}
		}

		double z = getCols()*t.getRows();
		double per = diff/z;
		if(averaged)
			per = per/9;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return Double.parseDouble(df.format(per));
	}
}
