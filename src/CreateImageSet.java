import java.util.*;
import java.io.PrintWriter;
import java.io.File;

public class CreateImageSet {
	
	public static void main(String[] args) {
		List<Integer> imgIdx = new ArrayList<>();
		for(int i=0; i<10; i++) {
			imgIdx.add(0);
		}
		try {
			Scanner csvScanner = new Scanner(new File("digits.csv"));
			while(csvScanner.hasNextLine()) {
				String line = csvScanner.nextLine();
				Scanner lineScanner = new Scanner(line);
				lineScanner.useDelimiter(",");
				int id = lineScanner.nextInt();
				int index = imgIdx.get(id);
				imgIdx.set(id, index+1);
				File file = new File("C:/Users/Justin Lee/Desktop/Main/Boston College/Senior/Semester 1/"
						            +"Algorithms/AlgProject/digits/"+id+"_"+index+".txt");
				file.getParentFile().mkdir();
				PrintWriter writer = new PrintWriter(file);
				int k = 0;
				while(lineScanner.hasNextInt()) {
					int x = lineScanner.nextInt();
					if(x < 128)
						writer.print("0 ");
					else
						writer.print("1 ");
					k = (k + 1)%28;
					if(k == 0)
						writer.println();
				}
				writer.close();
				lineScanner.close();
			}
			csvScanner.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
