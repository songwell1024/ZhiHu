package com.springboot.springboot.service;

import com.springboot.springboot.model.Question;
import org.apache.ibatis.annotations.Update;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WilsonSong
 * solr的搜索服务
 * @date 2018/7/25
 */
@Service
public class SearchService {

    //private static final String  SOLR_URL = "http://127.0.0.1:8983/solr/#/wenda";
    private static final String  SOLR_URL ="http://localhost:8983/solr/wenda";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";
    /**
     *
     * @param keyword 搜索的关键词
     * @param offset 翻页
     * @param count 翻页
     * @param hlPer  高亮的前缀
     * @param hlPos 高亮的后缀
     * @return
     * 搜索
     */
    public List<Question> searchQuestion(String keyword, int offset, int count, String hlPer, String hlPos) throws Exception{
        List<Question> questionList = new ArrayList<>();
        //solr的相关配置
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);         //高亮
        query.setHighlightSimplePre(hlPer);  //前缀
        query.setHighlightSimplePost(hlPos);  //后缀
        query.set("hl.fl", QUESTION_CONTENT_FIELD + "," +QUESTION_TITLE_FIELD);
        QueryResponse response = client.query(query);
        //解析搜索的界面的东西
        for (Map.Entry<String, Map<String, List<String>>> entry :response.getHighlighting().entrySet()){
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0){
                    q.setContent(contentList.get(0));
                }
            }

            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0){
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        return questionList;
    }

    /**
     * 索引
     * @param qid
     * @param title
     * @param content
     * @return
     */
    public boolean indexQuestion(int qid, String title, String content) throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField(QUESTION_CONTENT_FIELD, content);

        UpdateResponse response = client.add(doc,1000);
        return response != null && response.getStatus() == 0;
    }

}
