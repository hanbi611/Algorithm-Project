
public class Resizer {

	public static TextImage resize(TextImage txtImg, int newRow, int newCol) {
		int rows = txtImg.getRows();
		int cols = txtImg.getCols();
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
				img[row][col] = txtImg.getPixel(rk[row], ck[col]);
		return new TextImage(img, newRow, newCol);
	}
	
	//test method
	public static void main(String[] args) {
		TextImage t1 = new TextImage("digits/0_7.txt");
		TextImage t2 = Resizer.resize(t1, 17, 45);
		t1.print();
		t2.print();
	}
}
