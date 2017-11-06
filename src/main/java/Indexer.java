import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;

public class Indexer {
	private final static String USER = "root";
	private final static String PASSWORD = "dpchn";
	static String indexDir = "/home/durga/Desktop/luceneDemo/index";

	public static void main(String[] args) throws SQLException {
		Connection connection = getConnection();

		System.out.println("Successful....");

		String query = "select * from record";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			IndexWriter writer = CreateWriter();
			long startTime = new Date().getTime();
			while (rs.next()) {
				Document document = new Document();
				document.add(new TextField("url", rs.getString("urls").toString(), Field.Store.YES));
				document.add(new TextField("title", rs.getString("title"), Field.Store.YES));
				document.add(new TextField("heading", rs.getString("head").toString(), Field.Store.YES));
				document.add(new TextField("body", rs.getString("para").toString(), Field.Store.YES));
				document.add(new TextField("article", rs.getString("article").toString(), Field.Store.YES));
				writer.addDocument(document);
			}
			long endTime = new Date().getTime();
			writer.commit();
			writer.close();
			connection.close();
			System.out.println("Time taken to index : " + (endTime - startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crawler", USER, PASSWORD);
		} catch (Exception e) {
			System.out.println("Error while connnection....");
			e.printStackTrace();
		}
		return con;
	}

	static IndexWriter CreateWriter() {
		FSDirectory fsDirectory;
		IndexWriter writer = null;
		try {
			fsDirectory = FSDirectory.open(Paths.get(indexDir));
			IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
			writer = new IndexWriter(fsDirectory, conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return writer;
	}
}
