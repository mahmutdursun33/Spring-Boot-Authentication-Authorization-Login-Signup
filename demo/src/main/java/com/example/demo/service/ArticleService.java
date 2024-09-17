package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> findArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticleById(Long id) {
        articleRepository.deleteById(id);
    }
}
