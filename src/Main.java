import controller.MediaController;
import controller.PlaylistController;
import exception.DuplicateResourceException;
import model.*;
import repository.interfaces.CategoryRepository;
import repository.interfaces.MediaRepository;
import repository.interfaces.PlaylistRepository;
import repository.jdbc.JdbcCategoryRepository;
import repository.jdbc.JdbcMediaRepository;
import repository.jdbc.JdbcPlaylistRepository;
import service.impl.MediaServiceImpl;
import service.impl.PlaylistServiceImpl;
import service.interfaces.MediaService;
import service.interfaces.PlaylistService;
import utils.ReflectionUtils;
import utils.SortingUtils;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CategoryRepository categoryRepo = new JdbcCategoryRepository();
        MediaRepository mediaRepo = new JdbcMediaRepository();
        PlaylistRepository playlistRepo = new JdbcPlaylistRepository();

        MediaService mediaService = new MediaServiceImpl(mediaRepo, categoryRepo);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepo, mediaRepo);

        MediaController mediaController = new MediaController(mediaService);
        PlaylistController playlistController = new PlaylistController(playlistService);

        Category pop = categoryRepo.findByName("Pop").orElseGet(() -> categoryRepo.create(new Category("Pop")));

        MediaContentBase m1 = new Song("Starboy", 210, 0, new Category(pop.getId(), pop.getName()), "The Weeknd");
        MediaContentBase m2 = new Podcast("Tech Talk", 600, 1000, new Category(pop.getId(), pop.getName()), "Dias");

        System.out.println("=== Create media ===");
        MediaContentBase created1 = mediaController.create(m1);
        MediaContentBase created2 = mediaController.create(m2);
        System.out.println(created1.display());
        System.out.println(created2.display());

        System.out.println("\n=== Try duplicate (should throw DuplicateResourceException) ===");
        try {
            mediaController.create(new Song("Starboy", 210, 0, new Category(pop.getId(), pop.getName()), "X"));
        } catch (DuplicateResourceException e) {
            System.out.println("OK: " + e.getMessage());
        }

        System.out.println("\n=== List all ===");
        List<MediaContentBase> all = mediaController.getAll();
        all.forEach(System.out::println);

        System.out.println("\n=== Sorted by name (lambda) ===");
        mediaController.getAllSortedByName().forEach(System.out::println);

        System.out.println("\n=== Filter only SONG (lambda) ===");
        mediaController.filterByType(MediaType.SONG).forEach(System.out::println);

        System.out.println("\n=== Reflection (RTTI) ===");
        System.out.println(ReflectionUtils.inspect(created1));

        System.out.println("\n=== Playlist create + add media ===");
        Playlist pl = playlistController.create(new Playlist("My Playlist"));
        playlistController.addMedia(pl.getId(), created1.getId());
        playlistController.addMedia(pl.getId(), created2.getId());
        System.out.println("Playlist media ids: " + playlistController.listMediaIds(pl.getId()));

        System.out.println("\n=== Update media ===");
        created1.setName("Starboy (edit)");
        created1.setDurationSeconds(220);
        mediaController.update(created1);
        System.out.println(mediaController.getById(created1.getId()));

        System.out.println("\n=== Extra: sort by duration (lambda) ===");
        SortingUtils.sortByDuration(mediaController.getAll()).forEach(System.out::println);

        System.out.println("\nDONE.");
    }
}
