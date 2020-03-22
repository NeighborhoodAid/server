package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Article {
    private final int amount;
    private final String title;
    private final String description;
    private final boolean done;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Article(@JsonProperty("amount") int amount, @JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("done") boolean done) {
        this.amount = amount;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return amount == article.amount &&
                title.equals(article.title) &&
                Objects.equals(description, article.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, title, description);
    }
}
