package controller;

import model.MediaContentBase;
import model.MediaType;
import service.interfaces.MediaService;

import java.util.List;

public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public MediaContentBase create(MediaContentBase media) {
        return mediaService.create(media);
    }

    public MediaContentBase getById(int id) {
        return mediaService.getById(id);
    }

    public List<MediaContentBase> getAll() {
        return mediaService.getAll();
    }

    public List<MediaContentBase> getAllSortedByName() {
        return mediaService.getAllSortedByName();
    }

    public List<MediaContentBase> filterByType(MediaType type) {
        return mediaService.filterByType(type);
    }

    public MediaContentBase update(MediaContentBase media) {
        return mediaService.update(media);
    }

    public void delete(int id) {
        mediaService.delete(id);
    }
}
