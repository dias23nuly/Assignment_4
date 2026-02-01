package service.interfaces;

import model.MediaContentBase;
import model.MediaType;

import java.util.List;

public interface MediaService {
    MediaContentBase create(MediaContentBase media);
    MediaContentBase getById(int id);
    List<MediaContentBase> getAll();
    List<MediaContentBase> getAllSortedByName();
    List<MediaContentBase> filterByType(MediaType type);
    MediaContentBase update(MediaContentBase media);
    void delete(int id);
}
