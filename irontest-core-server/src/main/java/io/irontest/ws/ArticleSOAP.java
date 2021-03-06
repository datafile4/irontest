package io.irontest.ws;

import io.irontest.db.ArticleDAO;
import io.irontest.models.Article;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * Created by Zheng on 30/06/2015.
 */
@WebService
public class ArticleSOAP {
    private final ArticleDAO dao;

    public ArticleSOAP(ArticleDAO dao) {
        this.dao = dao;
    }

    @WebMethod
    public List<Article> findAll() {
        return dao.findAll();
    }

    @WebMethod
    public List<Article> findByCreationTime(@WebParam(name = "startTime") Date startTime,
                                            @WebParam(name = "endTime") Date endTime) {
        return dao.findByCreationTime(startTime, endTime);
    }

    @WebMethod
    public Article createArticle(@WebParam(name = "title") String title, @WebParam(name = "content") String content) {
        Article article = new Article(0, title, content, null, null);
        long id = dao.insert(article);
        return dao.findById(id);
    }

    @WebMethod
    public int updateArticleByTitle(@WebParam(name = "title") String title, @WebParam(name = "content") String content) {
        return dao.updateByTitle(title, content);
    }
}
