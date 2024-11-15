package club.management.club.services.publications;

import club.management.club.entities.Publication;
import org.springframework.data.jpa.domain.Specification;

public class PublicationSpecifications {
    public static Specification<Publication> withTitle(String title) {
        return (root, query, builder) ->
                (title == null || title.isEmpty())
                        ? null
                        : builder.like(root.get("title"), "%" + title + "%");
    }
}