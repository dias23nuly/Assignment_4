package utils;

import model.MediaContentBase;
import model.MediaType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class SortingUtils {
    private SortingUtils() {}

    // Lambdas for sorting/filtering
    public static List<MediaContentBase> sortByName(List<MediaContentBase> list) {
        return list.stream()
                .sorted(Comparator.comparing(m -> m.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<MediaContentBase> sortByDuration(List<MediaContentBase> list) {
        return list.stream()
                .sorted(Comparator.comparingInt(MediaContentBase::getDurationSeconds))
                .collect(Collectors.toList());
    }

    public static List<MediaContentBase> filterByType(List<MediaContentBase> list, MediaType type) {
        return list.stream()
                .filter(m -> m.getType() == type)
                .collect(Collectors.toList());
    }
}
