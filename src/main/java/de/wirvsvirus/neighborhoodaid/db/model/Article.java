package de.wirvsvirus.neighborhoodaid.db.model;

public class Article {
    private final int amount;
    private final String title;
    private final String description;
    private final boolean done;

    public Article(int amount, String title, String description, boolean done) {
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
}
