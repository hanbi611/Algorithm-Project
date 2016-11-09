import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class TextImage {
	//private List<List<Integer>> img;
	private int[][] img;
	private int rows, cols;
	
	public TextImage(BufferedImage buffImg) {
		//img = new ArrayList<>();
		cols = buffImg.getWidth();
		rows = buffImg.getHeight();
		img = new int[rows][cols];
		for(int y=0; y<rows; y++) {
			//List<Integer> temp = new ArrayList<>();
			for(int x=0; x<cols; x++) {
				if(isBlack(x, y, buffImg))
					//temp.add(1);
					img[y][x] = 1;
				else
					//temp.add(0);
					img[y][x] = 0;
			}
			//img.add(temp);
		}
	}
	
	public TextImage(int[][] img, int rows, int cols) {
		this.cols = cols;
		this.rows = rows;
		this.img = img;
	}
	
	public TextImage(int rows, int cols) {
		img = new int[rows][cols];
		this.rows = rows;
		this.cols = cols;
	}
	
	public TextImage(String pathname) {
		int[][] img;
		int cols = 0;
		int rows = 0;
		try {
			Scanner fileScanner = new Scanner(new File(pathname));
			while(fileScanner.hasNextLine()) {
				rows++;
				Scanner lineScanner = new Scanner(fileScanner.nextLine());
				while(lineScanner.hasNextInt()) {
					cols++;
					lineScanner.nextInt();
				}
				lineScanner.close();
			}
			cols = cols/rows;
			img = new int[rows][cols];
			fileScanner.close();
			fileScanner = new Scanner(new File(pathname));
			for(int row=0; row<rows; row++) {
				Scanner lineScanner = new Scanner(fileScanner.nextLine());
				for(int col=0; col<cols; col++) {
					int n = lineScanner.nextInt();
					img[row][col] = n;
				}
				lineScanner.close();
			}
			fileScanner.close();
			this.img = img;
			this.rows = rows;
			this.cols = cols;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int getColor(int x, int y, int color, BufferedImage img) {
		int value = img.getRGB(x, y) >> color & 0xff;
		return value;
	}
	
	public boolean isBlack(int x, int y, BufferedImage img) {
		return getColor(x, y, 16, img) == 0;
	}
	
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
}
