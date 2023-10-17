package io.collective.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.restsupport.BasicHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController extends BasicHandler {
    private final ArticleDataGateway gateway;

    public ArticlesController(ObjectMapper mapper, ArticleDataGateway gateway) {
        super(mapper);
        this.gateway = gateway;
    }

    @Override
    public void handle(String target, Request request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        get("/articles", List.of("application/json", "text/html"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *all* articles, map record to infos, and send back a collection of article infos
                final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                final ObjectMapper objectMapper = new ObjectMapper();

                List<ArticleRecord> gatewayListOfRecords = this.gateway.findAll();
                List<ArticleInfo> listOfInfos = new ArrayList<ArticleInfo>();
                for ( ArticleRecord currentRecord : gatewayListOfRecords){
                    ArticleInfo newInfo = new ArticleInfo(currentRecord.getId(), currentRecord.getTitle());
                    listOfInfos.add(newInfo);
                }

                writeJsonBody(servletResponse, listOfInfos);

            }
        });

        get("/available", List.of("application/json"), request, servletResponse, () -> {

            { // todo - query the articles gateway for *available* articles, map records to infos, and send back a collection of article infos
                final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                final ObjectMapper objectMapper = new ObjectMapper();

                List<ArticleRecord> gatewayListOfRecords = this.gateway.findAvailable();
                List<ArticleInfo> listOfInfos = new ArrayList<ArticleInfo>();
                for ( ArticleRecord currentRecord : gatewayListOfRecords){
                    ArticleInfo newInfo = new ArticleInfo(currentRecord.getId(), currentRecord.getTitle());
                    listOfInfos.add(newInfo);
                }

                writeJsonBody(servletResponse, listOfInfos);

            }
        });
    }
}
