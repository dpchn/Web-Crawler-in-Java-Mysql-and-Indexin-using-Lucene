import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PointRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SynonymQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Searcher {

	public static void main(String[] args) {
		System.out.println("Enter query to Search : ");
		Scanner scanner = new Scanner(System.in);
		String textToFind = scanner.nextLine();
		String indexDir = "/home/durga/Desktop/luceneDemo/index";
		try {
			FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexDir));

			IndexReader reader = DirectoryReader.open(fsDirectory);
			IndexSearcher searcher = new IndexSearcher(reader);
			// Query query = phraseQuery(textToFind);
			Query query1 = fuzzyQuery(textToFind);

			TopDocs docs = searcher.search(query1, 10);
			if (docs.totalHits == 0)
				System.out.println("Not found");
			else {
				for (ScoreDoc scoreDoc : docs.scoreDocs) {
					System.out.println(scoreDoc.score);
					System.out.println("Url :"+searcher.doc(scoreDoc.doc).get("url"));
					System.out.println("Title :"+searcher.doc(scoreDoc.doc).get("title"));
					System.out.println("Heading :"+searcher.doc(scoreDoc.doc).get("heading"));
					System.out.println("Body :"+searcher.doc(scoreDoc.doc).get("body"));
					System.out.println("Article :"+searcher.doc(scoreDoc.doc).get("article"));

				}
			}

			//////////////////////////
			Boostedquery(textToFind, searcher);
			RegexQUery(textToFind, searcher);
			///////////////////////////

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Query phraseQuery(String text) {
		Analyzer analyzer = new StandardAnalyzer();
		QueryBuilder builder = new QueryBuilder(analyzer);
		Query query = builder.createPhraseQuery("body", text);
		return query;
	}

	static FuzzyQuery fuzzyQuery(String text) {
		System.out.println("Using Fuzzy Query....");
		Analyzer analyzer = new StandardAnalyzer();
		FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("body", text));
		return fuzzyQuery;
	}


	static void Boostedquery(String text, IndexSearcher searcher) throws IOException {
		System.out.println("\n................Boosted Query........ ");
		SynonymQuery query = new SynonymQuery(new Term("body", text));

		BoostQuery boostQuery = new BoostQuery(query, 2.0f);
		TopDocs docs = searcher.search(boostQuery, 10);
		if (docs.totalHits == 0)
			System.out.println("Not found");
		else {
			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				System.out.println(scoreDoc.score);
				System.out.println("Url :"+searcher.doc(scoreDoc.doc).get("url"));
				System.out.println("Title :"+searcher.doc(scoreDoc.doc).get("title"));
				System.out.println("Heading :"+searcher.doc(scoreDoc.doc).get("heading"));
				System.out.println("Body :"+searcher.doc(scoreDoc.doc).get("body"));
				System.out.println("Article :"+searcher.doc(scoreDoc.doc).get("article"));
			}
		}

	}

	static void RegexQUery(String text, IndexSearcher searcher) throws IOException {
		System.out.println("\n----------------Regex query.....");
		Analyzer analyzer = new StandardAnalyzer();
		QueryBuilder builder = new QueryBuilder(analyzer);
		RegexpQuery regexpQuery = new RegexpQuery(new Term(text));
		TopDocs docs = searcher.search(regexpQuery, 10);

		System.out.println(regexpQuery.getRegexp());
		if (docs.totalHits == 0)
			System.out.println("Not found");
		else {
			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				System.out.println(scoreDoc.score);
				System.out.println("Url :"+searcher.doc(scoreDoc.doc).get("url"));
				System.out.println("Title :"+searcher.doc(scoreDoc.doc).get("title"));
				System.out.println("Heading :"+searcher.doc(scoreDoc.doc).get("heading"));
				System.out.println("Body :"+searcher.doc(scoreDoc.doc).get("body"));
				System.out.println("Article :"+searcher.doc(scoreDoc.doc).get("article"));
			}
		}
	}
}
