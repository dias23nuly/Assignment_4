package service.impl;

import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.MediaContentBase;
import model.MediaType;
import repository.interfaces.CategoryRepository;
import repository.interfaces.MediaRepository;
import service.interfaces.MediaService;
import utils.SortingUtils;

import java.util.List;

public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepo;          // DIP
    private final CategoryRepository categoryRepo;    // DIP

    public MediaServiceImpl(MediaRepository mediaRepo, CategoryRepository categoryRepo) {
        this.mediaRepo = mediaRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public MediaContentBase create(MediaContentBase media) {

        // validation in service (as required)
        try {
            media.validate();
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid media: " + e.getMessage());
        }

        // FK category must exist
        categoryRepo.findById(media.getCategory().getId())
                .orElseThrow(() -> new InvalidInputException(
                        "Category not found: id=" + media.getCategory().getId()
                ));

        // duplicate check
        if (mediaRepo.existsByNameAndType(media.getName(), media.getType())) {
            throw new DuplicateResourceException("Duplicate: " + media.getName() + " " + media.getType());
        }

        return mediaRepo.create(media);
    }

    @Override
    public MediaContentBase getById(int id) {
        return mediaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: id=" + id));
    }

    @Override
    public List<MediaContentBase> getAll() {
        return mediaRepo.findAll();
    }

    @Override
    public List<MediaContentBase> getAllSortedByName() {
        List<MediaContentBase> all = mediaRepo.findAll();
        return SortingUtils.sortByName(all);
    }

    @Override
    public List<MediaContentBase> filterByType(MediaType type) {
        List<MediaContentBase> all = mediaRepo.findAll();
        return SortingUtils.filterByType(all, type);
    }

    @Override
    public MediaContentBase update(MediaContentBase media) {
        if (media.getId() <= 0) throw new InvalidInputException("media.id must be > 0 for update");

        mediaRepo.findById(media.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: id=" + media.getId()));

        try {
            media.validate();
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid media: " + e.getMessage());
        }

        categoryRepo.findById(media.getCategory().getId())
                .orElseThrow(() -> new InvalidInputException(
                        "Category not found: id=" + media.getCategory().getId()
                ));

        return mediaRepo.update(media);
    }

    @Override
    public void delete(int id) {
        mediaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: id=" + id));
        mediaRepo.delete(id);
    }
}
