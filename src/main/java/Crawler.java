import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	HashSet<String> links = new HashSet<String>();

	void getLinks(String rootUrl) {
		if (!links.contains(rootUrl)) {
			links.add(rootUrl);
			System.out.println(rootUrl);
			getArticles(rootUrl);
			
			try {
				//Parse html contents
				Document document = Jsoup.connect(rootUrl).get();
				//getting links
				System.out.println("Body : "+ document.body().text());
				Elements elements = document.select("a[href]");
				Elements elements2 = document.select("head");
				
				
/*
				Elements elements2 = document.select("article");
				Elements elements2 = document.select("p");
				System.out.println("Bod : "+ elements2.text());*/
				System.out.println("Bod : "+ elements2.text());
				for(Element page : elements) {
					
					getLinks(page.attr("abs:href"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	 public void getArticles(String x) {
	     
	            Document document;
	            try {
	                document = Jsoup.connect(x).get();
	                Elements articleLinks = document.select("h2 a[href]");
	              
	                for (Element article : articleLinks) {
	                	System.out.println();
	                  
	                        //Remove the comment from the line below if you want to see it running on your editor,
	                        //or wait for the File at the end of the execution
	                        //System.out.println(article.attr("abs:href"));

	                        ArrayList<String> temporary = new ArrayList<String>();
	                        temporary.add(article.text()); //The title of the article
	                        System.out.println("Title : "+ article.text());
	                       
	                   
	                }
	                System.out.println();
	            } catch (IOException e) {
	                System.err.println(e.getMessage());
	            }

	    }
	
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.getLinks("http://www.mit.edu");
		
	}

}
