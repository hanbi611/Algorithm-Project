import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class Test {

	public static void main (String[] args){
		
		File inputFolder1 = new File(args[0]); 
		File[] files = inputFolder1.listFiles();
		File f = files[0];
		TextImage ti = new TextImage(f);
		// test cancelNoise
		ti.cancelNoise();
		ti.print();
		System.out.println();
		
		// test crop
		TextImage cropped = ti.crop();
		cropped.print();
	}
}
