package solr.learning.lucene.multivalue;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneMultiValue {
	private String dirName =  "lucene_multivalue";
	
	public LuceneMultiValue(){
		
	}
	
	public void buildIndex() throws IOException{
		Directory dir = new SimpleFSDirectory(new File(dirName));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer );
		IndexWriter writer = new IndexWriter(dir, config);
		
		Document doc = new Document();
//		doc.add(new StoredField("name", "abc"));
		doc.add(new StringField("name","abc",Store.YES));
		writer.addDocument(doc );
		
	}
	
	public void search(){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
