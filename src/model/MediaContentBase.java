package model;

import service.interfaces.Validatable;
import service.interfaces.Playable;
import service.interfaces.PricedItem;

public abstract class MediaContentBase implements Validatable<MediaContentBase>, Playable, PricedItem {
    private int id;
    private String name;
    private int durationSeconds;
    private int priceKzt;

    // Composition / Aggregation: Media -> Category
    private Category category;

    protected MediaContentBase() {}

    protected MediaContentBase(String name, int durationSeconds, int priceKzt, Category category) {
        this.name = name;
        this.durationSeconds = durationSeconds;
        this.priceKzt = priceKzt;
        this.category = category;
    }

    // abstract methods (минимум 2) :contentReference[oaicite:6]{index=6}
    public abstract MediaType getType();
    public abstract String getDetails();

    // concrete method (минимум 1)
    public String display() {
        return "[" + getType() + "] " + name + " (" + durationSeconds + "s, " + formatPrice() + "), category=" +
                (category == null ? "null" : category.getName());
    }

    // Polymorphism via overridden methods in children
    @Override
    public void play() {
        System.out.println("Playing: " + display());
    }

    // Validatable<T>
    @Override
    public void validate() {
        if (Validatable.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("durationSeconds must be > 0");
        }
        if (priceKzt < 0) {
            throw new IllegalArgumentException("priceKzt must be >= 0");
        }
        if (category == null || category.getId() <= 0) {
            throw new IllegalArgumentException("category must exist (category_id > 0)");
        }
    }

    // getters/setters (encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    @Override
    public int getPriceKzt() { return priceKzt; }
    public void setPriceKzt(int priceKzt) { this.priceKzt = priceKzt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return display();
    }
}
