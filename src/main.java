import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

public class main {

	/* data needed to save:
	 * Title
	 * Writer
	 * Desk
	 * Month
	 * URL
	 */
	
	//Map which contains the names of each desk and the corresponding URL part needed to access each
	static Map<String, String> desks = Stream.of(new String[][] {
		{"campus","University"},
		{"city-county","City"},
		{"state","State"},
		{"sports","Sports"},
		{"culture","Arts and Culture"},
		{"opinion","Opinion"},
		{"elevate","Elevate"},
		{"coronavirus","COVID-19"}
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	/* Format of data internal arrays:
	 * [0] Title
	 * [1] Writer
	 * [2] Desk
	 * [3] Month
	 * [4] Word Count
	 * [5] URL
	 */
	static ArrayList<String[]> data = new ArrayList<String[]>();
	
	public static void main(String[] args) {

		//boolean running = true;
		int page_number = 0;
		int article_number = 1;
		for(int k = 0; k < 100; k++) {
			page_number++;
			Document page_doc = load(desks.get("campus"),page_number);
			Elements articles = page_doc.getElementsByTag("article");
			if(articles.size() == 1) {
				//running = false;
				break;
			}

			//selects second container-fluid, first container-fluid holds four featured articles which also appear in the second container-fluid
			Elements e = page_doc.getElementsByClass("container-fluid");
			Element page = e.last();


			Elements headlines = page.getElementsByClass("headline");
			Elements datelines = page.getElementsByClass("dateline");

			int len = headlines.indexOf(headlines.last()) + 1;

			for(int i = 0; i < len; i++) {
				System.out.println(article_number);
				article_number++;
				data.add(getData(headlines,datelines,i));
			}
		}
		
		generateCSV();
	}
	
	public static Document load(String desk, int page) {
		Document doc = Jsoup.parse("");
		
		try {doc = Jsoup.connect("https://www.dailytarheel.com/section/" + desk + "?page=" + page +"&per_page=20").get();}
		catch(IOException e) {}
		
		return doc;
	}
	
	public static String[] getData(Elements headlines, Elements datelines, int i) {
		String[] output = new String[5];
		output[0] = headlines.get(i).text();
		output[1] = datelines.get(i).getElementsByTag("a").text();
		output[2] = "University";
		output[4] = headlines.get(i).getElementsByTag("a").attr("href");
		//reads the article's publication month and year from the URL
		output[3] = output[4].substring(37, 44);
		
		return output;
	}
	
	public static String getWordCount(String link) {
		Document doc = Jsoup.parse("");
		
		try {doc = Jsoup.connect(link).get();}
		catch(IOException e) {}
		
		Elements article = doc.getElementsByClass("article-content");
		String str = article.text();
		str = str.trim();
		if(str.isEmpty()) return "0";
		
		return String.valueOf(str.split("\\s+").length-25);
	}
	
	public static void generateCSV() {
		File output = new File("output.csv");
		
		try {
			FileWriter writer_f = new FileWriter(output);
			CSVWriter writer = new CSVWriter(writer_f);
			writer.writeAll(data);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	private static class Counter extends Thread{
		Counter() {
			
		}
		void main(String[] args) {
			Counter thread = new Counter();
		}
	}
}
