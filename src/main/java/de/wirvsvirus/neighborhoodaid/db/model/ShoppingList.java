package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ShoppingList {

    private final UUID id;
    private final UUID creator;
    private final UUID claimer;
    private final long creationDateTime;
    private final long dueDateTime;
    private final List<Article> articles;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ShoppingList(@JsonProperty("id") UUID id, @JsonProperty("creator") UUID creator, @JsonProperty("claimer") UUID claimer, @JsonProperty("creationDateTime") long creationDateTime, @JsonProperty("dueDateTime") long dueDateTime, @JsonProperty("articles") List<Article> articles) {
        this.id = id;
        this.creator = creator;
        this.claimer = claimer;
        this.creationDateTime = creationDateTime;
        this.dueDateTime = dueDateTime;
        this.articles = articles;
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

    public List<Article> getArticles() {
        return articles;
    }

    public ShoppingList asNew(UUID uuid, UUID creator) {
        return new ShoppingList(uuid, creator, null, creationDateTime, dueDateTime, articles);
    }

    public ShoppingList withNewVariables(ShoppingList newList) {
        return new ShoppingList(id, creator, claimer, newList.creationDateTime, newList.dueDateTime, newList.articles);
    }

    public ShoppingList withClaimer(UUID user) {
        return new ShoppingList(id, creator, user, creationDateTime, dueDateTime, articles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return id.equals(that.id) &&
                creator.equals(that.creator) &&
                Objects.equals(claimer, that.claimer) &&
                Objects.equals(creationDateTime, that.creationDateTime) &&
                Objects.equals(dueDateTime, that.dueDateTime) &&
                Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, claimer, creationDateTime, dueDateTime, articles);
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", creator=" + creator +
                ", claimer=" + claimer +
                ", creationDateTime=" + creationDateTime +
                ", dueDateTime=" + dueDateTime +
                ", articles=" + articles +
                '}';
    }
}
