package de.wirvsvirus.neighborhoodaid.db.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class ShoppingList {

    private final UUID creator;
    private final UUID claimer;
    private final ZonedDateTime creationDateTime;
    private final ZonedDateTime dueDateTime;
    private final List<Article> articles;

    public ShoppingList(UUID creator, UUID claimer, ZonedDateTime creationDateTime, ZonedDateTime dueDateTime, List<Article> articles) {
        this.creator = creator;
        this.claimer = claimer;
        this.creationDateTime = creationDateTime;
        this.dueDateTime = dueDateTime;
        this.articles = articles;
    }

    public boolean addArticle(Article article) {
        return articles.add(article);
    }

    public boolean removeArticle(Article article) {
        return articles.remove(article);
    }

    public UUID getCreator() {
        return creator;
    }

    public UUID getClaimer() {
        return claimer;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public ZonedDateTime getDueDateTime() {
        return dueDateTime;
    }
}
