package com.example.controller;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.KnowledgeSearchService;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;


@Controller
@RequestMapping(value="/knowledgeSearch")
public class KnowledgeSearchController {
	
	private String ES_HOST = "127.0.0.1";
	private int ES_PORT = 9300;
	private String ES_INDEX = "120ask";
	private String ES_TYPE = "question";
	
	@Autowired
	private KnowledgeSearchService knowledgeSearchService;

	@RequestMapping(value ="/search",method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> search(
			@RequestParam("q")String q, 
			@RequestParam(value="start", defaultValue="0")int start, 
			@RequestParam(value="count", defaultValue="10")int count
	){
		
		// 编码转换，应该有更好的方法
		byte qb[]; 
        try {
			qb = q.getBytes("ISO-8859-1");
			q = new String(qb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		org.elasticsearch.common.settings.Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true).build();	
	    TransportClient client = new TransportClient(settings);
	    client.addTransportAddress(new InetSocketTransportAddress(ES_HOST, ES_PORT));
	    
	    SearchResponse response = client.prepareSearch(ES_INDEX)
	            .setTypes(ES_TYPE)
	            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
	            .setQuery(QueryBuilders.matchQuery("_all", q))             // Query
	            // .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
	            .setFrom(start).setSize(count).setExplain(true)
	            .execute()
	            .actionGet();
	    
	    
	    SearchHits hits = response.getHits();
	    List<Map<String, Object>> matchRsult = new LinkedList<Map<String, Object>>();
	    for (SearchHit hit : hits.getHits())
	    {
	        matchRsult.add(hit.getSource());
	    }
	    
	    client.close();

		return matchRsult;
	}
	
	@RequestMapping(value ="/query",method=RequestMethod.GET)
	public @ResponseBody String query(@RequestParam("q")String q) {
		// 编码转换，应该有更好的方法
		byte qb[]; 
        try {
			qb = q.getBytes("ISO-8859-1");
			q = new String(qb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return knowledgeSearchService.queryInference(q);
	}
}
