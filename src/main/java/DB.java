import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DB {

	static Connection getConnection() {
		final String USER = "root";
		final String PASSWORD = "dpchn";
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crawler?useSSL=false", USER, PASSWORD);
		} catch (Exception e) {
			System.out.println("Error while connnection....");
			e.printStackTrace();
		}
		return con;
	}

	HashSet<String> links = new HashSet<String>();
	ArrayList<String> temporary = new ArrayList<String>();
	void getLinks(String rootUrl) {
		if (!links.contains(rootUrl)) {
			links.add(rootUrl);
			System.out.println(rootUrl);
			
			try {
				Document document  = Jsoup.connect(rootUrl).get();
				/*PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, rootUrl);
				// statement.setString(2, arg1);
				statement.executeUpdate();*/
				
				Elements elements = document.select("a[href]");
				Elements heading = document.select("head");
				Elements article = document.select("article");
				Elements body = document.select("p");
				System.out.println("Heading : "+ heading.text());
				System.out.println("Body : "+ body.text());
				System.out.println("Article : "+ article.text());
				
				getTitle(rootUrl, heading.text(), body.text(), article.text());
				for (Element page : elements) {
					getLinks(page.attr("abs:href"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	
	
	
	public void getTitle(String url, String head, String body, String article) throws SQLException {
		Document document;
		try {
			String sql = "INSERT INTO  record (urls, title, head,para,article ) VALUES (?,?,?,?,?)";
			PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			
			document = Jsoup.connect(url).get();
			Elements articleLinks = document.select("h2 a[href]");

			for (Element title : articleLinks) {
				if (!temporary.contains(title.text())) {
					temporary.add(title.text()); // The title of the article
					System.out.println("Title : " + title.text());
					statement.setString(1, url);
					statement.setString(2, title.text().trim());
					statement.setString(3, head.trim());
					statement.setString(4, body.trim());
					statement.setString(5, article.trim());
					statement.executeUpdate();
				}
			}
			
			System.out.println();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		new DB().getLinks("http://www.mit.edu");

	}
}
