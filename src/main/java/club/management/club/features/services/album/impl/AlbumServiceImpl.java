package club.management.club.features.services.album.impl;

import club.management.club.features.entities.Album;
import club.management.club.features.entities.Image;
import club.management.club.features.repositories.AlbumRepo;
import club.management.club.features.services.album.AlbumService;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepo albumRepo;
    @Override
    public Album save(Album album) {
        return albumRepo.save(album);
    }

    @Override
    public void addImageToAlbum(String albumId,Image image) {
        Album album = albumRepo.findById(albumId).orElseThrow(
                () -> new ResourceNotFoundException("Album","albumId",albumId)
        );
        if (album.getAlbumImages() ==null){
            album.setAlbumImages(List.of(image));
        }else {
            album.getAlbumImages().add(image);
        }
    }

    @Override
    public List<Album> getClubAlbums(String clubId) {
        return albumRepo.findByClub_Id(clubId);
    }

    @Override
    public Album findById(String albumId) {
        return albumRepo.findById(albumId).orElseThrow(
                () -> new ResourceNotFoundException("Album","albumId",albumId)
        );
    }
}
