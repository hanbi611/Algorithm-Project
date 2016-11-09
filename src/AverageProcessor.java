
public class AverageProcessor implements TextImageProcessor {

	public TextImage process(TextImage txtImg) {
		int cols = txtImg.getCols();
		int rows = txtImg.getRows();
		//System.out.println(cols);
		//System.out.println(rows);
		TextImage avg = new TextImage(rows, cols);
		//int[][] avg = new int[rows][cols];
		for(int col=0; col<cols; col++) {
			int[] cs = new int[] {col-1, col, col+1};
			for(int row=0; row<rows; row++) {
				int[] rs = new int[] {row-1, row, row+1};
				int sum = 0;
				for(int c : cs) {
					if(!((c < 0) || (c >= cols)))
						for(int r : rs) {
							if(!((r < 0) || (r >= rows))) {
								sum += txtImg.getPixel(r, c);
							}
						}
				avg.setPixel(sum, row, col);
				//avg[row][col] = sum;
				}
			}
		}
		//return new TextImage(avg, rows, cols);
		return avg;
	}
}
