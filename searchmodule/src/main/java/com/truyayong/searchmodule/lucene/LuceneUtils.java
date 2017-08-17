package com.truyayong.searchmodule.lucene;

import android.os.Environment;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alley_qiu on 2017/3/8.
 */

public class LuceneUtils {

    //Bmob最多支持一次查询不能超过1204个字节，所以只选取排名最高的50项
    private final static int USEFUL_ITEM_COUNT = 50;

    public static void saveDoc(String objectId, String title, String content) throws IOException {
        //初始化语料分析器与索引存储地址
        Analyzer analyzer = new IKAnalyzer();
                //new StandardAnalyzer(Version.LUCENE_CURRENT);
        File file = new File(Environment.getExternalStorageDirectory(), "com.truyayong.gatherstory.luceneindex");
        Directory directory = FSDirectory.open(file);

        //生成Document，写入索引
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        Document doc = generateDoc(objectId, title, content);
        indexWriter.addDocument(doc);
        indexWriter.close();

    }

    private static Document generateDoc(String objectId, String title, String content) {
        Document doc = new Document();
        doc.add(new Field("objectId", objectId, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
        return doc;
    }

    public static List<String> searchKeyword(String keyword) throws IOException, ParseException {

        List<String> objectIds = new LinkedList<>();
        Analyzer analyzer = new IKAnalyzer();
                //new StandardAnalyzer(Version.LUCENE_CURRENT);
        File file = new File(Environment.getExternalStorageDirectory(), "com.truyayong.gatherstory.luceneindex");
        Directory directory = FSDirectory.open(file);
        IndexReader indexReader =IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);

        String[] fields = {"title", "content"};
        QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_CURRENT, fields, analyzer);
        Query query = parser.parse(keyword);
        ScoreDoc[] hits = searcher.search(query, null, 1000).scoreDocs;
        for (int i = 0; i < hits.length && i < USEFUL_ITEM_COUNT; i++) {
            Document hitDoc = searcher.doc(hits[i].doc);
            objectIds.add(hitDoc.get("objectId"));
        }
        searcher.close();
        indexReader.close();
        directory.close();
        return objectIds;
    }
}
