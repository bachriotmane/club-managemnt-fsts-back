package club.management.club.features.services.album;

import club.management.club.features.entities.Album;
import club.management.club.features.entities.Image;

import java.util.List;

public interface AlbumService {
    Album save(Album album) ;
    void addImageToAlbum(String albumId,Image image);
    List<Album> getClubAlbums(String clubId);
    Album findById(String albumId);
}
