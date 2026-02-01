package model;

public class Podcast extends MediaContentBase {
    private String host;

    public Podcast() {}

    public Podcast(String name, int durationSeconds, int priceKzt, Category category, String host) {
        super(name, durationSeconds, priceKzt, category);
        this.host = host;
    }

    @Override
    public MediaType getType() {
        return MediaType.PODCAST;
    }

    @Override
    public String getDetails() {
        return "host=" + (host == null ? "unknown" : host);
    }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
}
