package model;

public class Song extends MediaContentBase {
    private String artist;

    public Song() {}

    public Song(String name, int durationSeconds, int priceKzt, Category category, String artist) {
        super(name, durationSeconds, priceKzt, category);
        this.artist = artist;
    }

    @Override
    public MediaType getType() {
        return MediaType.SONG;
    }

    @Override
    public String getDetails() {
        return "artist=" + (artist == null ? "unknown" : artist);
    }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
}
