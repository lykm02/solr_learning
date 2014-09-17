package solr.learning.lucene.multivalue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneMultiValue {
	private String dirName =  "lucene_multivalue";
	
	public LuceneMultiValue(){
		
	}
	
	public void buildIndex() throws IOException{
		Directory dir = new SimpleFSDirectory(new File(dirName));
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_47);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer );
		IndexWriter writer = new IndexWriter(dir, config);
		
		FieldType idType = new FieldType();
		idType.setStored(true);
		idType.setIndexed(true);
		
		FieldType nameType = new FieldType();
		nameType.setTokenized(true);
		nameType.setIndexed(true);
		nameType.setStored(true);
		
		Document doc = new Document();
		doc.add(new Field("name","abc bdc", nameType ));
		doc.add(new Field("name","efg fgh", nameType ));
		doc.add(new Field("name","xyz yabc", nameType ));
		doc.add(new Field("id","100", idType ));
//		doc.add(new Field("name","xyz yabc",Store.YES));
		writer.addDocument(doc );
		doc = new Document();
		doc.add(new StringField("name","while for",Store.YES));
		doc.add(new StringField("name","if else",Store.YES));
		doc.add(new StringField("name","{ }",Store.YES));

		doc.add(new Field("id","101", idType ));
		writer.addDocument(doc );
		writer.commit();
		writer.close();
	}
	
	public void search() throws IOException, ParseException{
		IndexReader r = DirectoryReader.open(FSDirectory.open(new File(dirName)));
		IndexSearcher searcher = new IndexSearcher(r);
		QueryParser qp = new QueryParser(Version.LUCENE_47, "name", new WhitespaceAnalyzer(Version.LUCENE_47));
		Query q = qp.parse("xyz");
		System.out.println("classic : " + q);
		Query query =  new TermQuery(new Term("name","xyz"));
		TopDocs topDocs = searcher.search(query , 10);
		if(topDocs.totalHits > 0){
			for(ScoreDoc scoreDoc: topDocs.scoreDocs){
				Document doc = searcher.doc(scoreDoc.doc);
				System.out.println(scoreDoc + " .. " + Arrays.asList(doc.getValues("id")) + Arrays.asList(doc.getValues("name")));
			}
		}else{
			System.out.println("Nothing found");
		}
	
	}
	
	public void clear(){
		File file = new File(dirName);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LuceneMultiValue val = new LuceneMultiValue();
		try {
			val.buildIndex();
			val.search();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
