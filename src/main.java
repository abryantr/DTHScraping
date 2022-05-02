import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {

	/* data needed to save:
	 * Title
	 * Writer
	 * Desk
	 * Month
	 * URL
	 */
	
	static String[] desks = new String[] {"campus","city-county","state","sports","culture","opinion","elevate","coronavirus"};
	
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

		boolean running = true;
		int page_number = 0;
		int article_number = 1;
		while(running) {
			page_number++;
			Document page_doc = load(desks[0],page_number);
			Elements articles = page_doc.getElementsByTag("article");
			if(articles.size() == 1) {
				running = false;
				break;
			}

			//selects second container-fluid, first container-fluid holds four featured articles which also appear in the second container-fluid
			Elements e = page_doc.getElementsByClass("container-fluid");
			Element page = e.last();
			ArrayList<String[]> page_data = new ArrayList<String[]>();


			Elements headlines = page.getElementsByClass("headline");
			Elements datelines = page.getElementsByClass("dateline");

			int len = headlines.indexOf(headlines.last()) + 1;

			for(int i = 0; i < len; i++) {
				String[] temp = new String[6];
				temp[0] = headlines.get(i).text();
				temp[1] = datelines.get(i).getElementsByTag("a").text();
				temp[2] = "University";
				temp[5] = headlines.get(i).getElementsByTag("a").attr("href");
				temp[4] = getWordCount(temp[5]);
				temp[3] = temp[5].substring(37, 44);
				
				System.out.println(article_number);
				article_number++;

//				for(int j = 0; j < 6; j++) {
//					System.out.println(temp[j]);
//				}
//				System.out.println();
				page_data.add(temp);
			}
		}
	}
	
	public static Document load(String desk, int page) {
		Document doc = Jsoup.parse("");
		
		try {doc = Jsoup.connect("https://www.dailytarheel.com/section/" + desk + "?page=" + page +"&per_page=20").get();}
		catch(IOException e) {}
		
		return doc;
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
}
