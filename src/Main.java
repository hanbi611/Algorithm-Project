import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.io.PrintWriter;

public class Main {
	/* TO DO:
	 * 	1. FIX writeOutput to the correct format
	 * 	2. finish processImages once other methods are finished. 
	 */
	final static int MAX = 1000;
	public static void main(String[] args) {

		File inputFolder1 = new File(args[0]); // 
		File inputFolder2 = new File(args[1]); //dataset folder
		File outputFolder = new File(args[2]); //output folder
		int k = Integer.parseInt(args[3]);

		List<TextImage> queries = getQueries(inputFolder1);
		int height = queries.get(0).getCols();
		int width = queries.get(0).getRows();
		
		processImages(queries,height,width);
		
		List<TextImage> samples = getSamples(inputFolder2);
		processImages(samples,height,width);
		
		List<TextImage> result;
		for(TextImage query : queries) {
			result = processQuery(query, samples, k);
			writeOutput(query, result, outputFolder);
		}
	}
	
	/**
	 * This method will process the images so that 
	 * all images are somewhat in similar format for accurate comparison
	 * @param images
	 */
	private static void processImages(List<TextImage> images, int height, int width){
		for (TextImage img : images){
			// Step 1: Cancel out noise
			img.cancelNoise();
			// Step 2: Crop
			img = img.crop();
			// Step 3: Center
			// Step 4: Align
			// Step 5: (Re)Center
			// Step 6: Resize
			img = img.resize(width, height);
			// Step 7: Average image
			img = img.average();
		}		
	}
	/** This method turns txt files of folder 2 into TextImage type. 
	 * If there are less than 1000 files in the folder, all images will be returned. 
	 * If there are more than 1000 files, 1000 files will be chosen to be compared 
	 * at random. So in any case, there will be less than or equal to 1000 comparisons. 
	 * @param inputfolder2
	 * @return list of TextImage in input folder 2
	 */
	public static List<TextImage> getSamples(File inputfolder2) { 
		File[] files = inputfolder2.listFiles();
		List<TextImage> samples = new ArrayList<>();
		
		if (files.length <= MAX)
			for (File f : files)
				samples.add(new TextImage(f));
		else{
			Set<Integer> s = new HashSet<>(100);
			Random r = new Random();
			while(s.size() < 1000)
				s.add(r.nextInt(files.length));
			for(Integer i : s)
				samples.add(new TextImage(files[i]));
		}
		return samples;
	}

	/** This method changes all the query files in input folder 1 into TextImage type.	 * 
	 * @param inputfolder1
	 * @return list of TextImage in input folder 1
	 */
	public static List<TextImage> getQueries(File inputfolder1) {
		File[] files = inputfolder1.listFiles();
		List<TextImage> queries = new ArrayList<>();
		for(File f : files)
			queries.add(new TextImage(f));
		return queries;
	}

	public static List<TextImage> processQuery(TextImage input, Collection<TextImage> dataset, int k) {
		PriorityQueue<Double> pq = new PriorityQueue<>();
		Map<Double, List<TextImage>> m = new HashMap<>(100);
		List<TextImage> result = new ArrayList<>();
		for(TextImage t : dataset) {
			double c = compare(input, t);
			List<TextImage> l;
			if(m.get(c) == null)
				l = new ArrayList<>();
			else
				l = m.get(c);
			l.add(t);
			m.put(c, l);
			pq.add(c);
		}
		int i = 0;
		while(i < k) {
			if(pq.isEmpty()) break;
			double val = pq.remove();
			List<TextImage> l = m.get(val);
			for(TextImage t : l) {
				result.add(t);
				i++;
				if(i == k) break;
			}
		}
		return result;
		/*int cat = input.categorize();
		Collection<TextImage> categorizedList = getCategorizedList(dataset, cat);
		Map<Double, List<TextImage>> m = new HashMap<>(100);
		PriorityQueue<Double> pq = new PriorityQueue<>();
		for(TextImage t : categorizedList) {
			double c = compare(input, t);
			List<TextImage> l;
			if(m.get(c) == null)
				l = new ArrayList<>();
			else
				l = m.get(c);
			l.add(t);
			m.put(c, l);
			pq.add(c);
		}
		List<TextImage> result = new ArrayList<>();
		int i = 0;
		while(i < k) {
			if(pq.isEmpty())
				break;
			double val = pq.remove();
			List<TextImage> l = m.get(val);
			for(TextImage t : l) {
				result.add(t);
				i++;
				if(i == k)
					break;
			}
		}
		return result;*/
	}

	public static Collection<TextImage> getCategorizedList(Collection<TextImage> imgs, int cat) {
		Collection<TextImage> categorizedList = new ArrayList<>();
		for(TextImage img : imgs)
			if(img.categorize() == cat)
				categorizedList.add(img);
		return categorizedList;
	}

	public static double compare(TextImage t1, TextImage t2) {
		double diff2 = 0;

		for(int col=0; col<t1.getCols(); col++) {
			for(int row=0; row<t1.getRows(); row++) {
				int pix1 = t1.getPixel(row, col);
				int pix2 = t2.getPixel(row, col);
				diff2 += Math.abs(pix1-pix2);
			}
		}

		double z = t1.getCols()*t2.getRows();
		double per = diff2/z;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return Double.parseDouble(df.format(per));
	}

	public static void writeOutput(TextImage input, List<TextImage> output, File directory) {
		File file = new File(directory.getPath() + "/" + input.toString() + ".txt");
		file.getParentFile().mkdir();
		try {
			PrintWriter writer = new PrintWriter(file);
			for(TextImage t : output)
				writer.println(t.toString());
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}