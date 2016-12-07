import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		File inputFolder1 = new File(args[0]); // File[] listoffiles = folder.listFiles();
		File inputFolder2 = new File(args[1]); //dataset folder
		File outputFolder = new File(args[2]); //output folder
		int k = Integer.parseInt(args[3]);
		
		List<TextImage> queries = getQueries(inputFolder1);
		List<TextImage> samples = getSamples(inputFolder2);
		preprocess(samples);
		List<TextImage> result;
		for(TextImage query : queries) {
			result = processQuery(query, samples, k);
			writeOutput(query, result, outputFolder);
		}
	}
	
	public static List<TextImage> getSamples(File dataset) {
		File[] files = dataset.listFiles();
		List<TextImage> samples = new ArrayList<>();
		Set<Integer> s = new HashSet<>();
		Random r = new Random();
		while(s.size() < 1000)
			s.add(r.nextInt(files.length));
		for(Integer i : s)
			samples.add(new TextImage(files[i]));
		return samples;
	}
	
	public static List<TextImage> getQueries(File inputFolder) {
		File[] files = inputFolder.listFiles();
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