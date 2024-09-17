package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAllArticles());
        return "article-list";
    }
    @GetMapping("/show")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAllArticles());
        return "show";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        return "article-form";
    }

    @PostMapping("/create")
    public String createArticle(@ModelAttribute("article") Article article) {
        articleService.saveArticle(article);
        return "redirect:/create";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
        model.addAttribute("article", article);
        return "article-form";
    }

    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable("id") Long id, @ModelAttribute("article") Article article) {
        article.setId(id);
        articleService.saveArticle(article);
        return "redirect:/show";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);
        return "redirect:/show";
    }
}
