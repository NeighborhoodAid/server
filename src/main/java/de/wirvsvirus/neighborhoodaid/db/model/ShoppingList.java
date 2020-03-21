package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class ShoppingList {

    private final UUID id;
    private final UUID creator;
    private final UUID claimer;
    private final Long creationDateTime;
    private final Long dueDateTime;
    private final List<Article> articles;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ShoppingList(@JsonProperty("id") UUID id, @JsonProperty("creator") UUID creator, @JsonProperty("claimer") UUID claimer, @JsonProperty("creationDateTime") Long creationDateTime, @JsonProperty("dueDateTime") Long dueDateTime, @JsonProperty("articles")List<Article> articles) {
        this.id = id;
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

    public UUID getId() {
        return id;
    }

    public UUID getCreator() {
        return creator;
    }

    public UUID getClaimer() {
        return claimer;
    }

    public Long getCreationDateTime() {
        return creationDateTime;
    }

    public Long getDueDateTime() {
        return dueDateTime;
    }

    protected ShoppingList withId(UUID newId) {
        return new ShoppingList(newId, creator, claimer, creationDateTime, dueDateTime, articles);
    }

    protected ShoppingList withCreator(UUID user) {
        return new ShoppingList(id, user, claimer, creationDateTime, dueDateTime, articles);
    }

    protected ShoppingList withClaimer(UUID user) {
        return new ShoppingList(id, creator, user, creationDateTime, dueDateTime, articles);
    }
}
