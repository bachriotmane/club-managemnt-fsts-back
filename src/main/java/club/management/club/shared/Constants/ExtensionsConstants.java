package club.management.club.shared.Constants;

import java.util.Map;
import java.util.Set;


public class ExtensionsConstants {
        public static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png"
            );
        public static final Map<String, String> EXTENSION_TO_MIME_TYPE = Map.of(
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "gif", "image/gif"
    );
}
