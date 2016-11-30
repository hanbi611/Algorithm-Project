
public class NoiseProcessor implements TextImageProcessor {

	//This merely sets all pixels with a value of 2 or less to 0
	//Should use after doing an averageProcess
	
	public TextImage process(TextImage txtImg) {
		TextImage img = new TextImage(txtImg.getImg(), txtImg.getRows(), txtImg.getCols());
		for(int row=0; row<txtImg.getRows(); row++)
			for(int col=0; col<txtImg.getCols(); col++)
				if(txtImg.getPixel(row, col) < 3)
					img.setPixel(0, row, col);
		return img;
	}

}
