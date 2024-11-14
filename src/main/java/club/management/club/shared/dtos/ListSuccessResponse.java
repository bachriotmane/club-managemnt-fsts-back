package club.management.club.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
// response use in all Listing
@Data
@AllArgsConstructor
public class ListSuccessResponse<T> {
    private Set<T> data;
    long totalItems;
    int totalPages;
    boolean hasNext;
}
