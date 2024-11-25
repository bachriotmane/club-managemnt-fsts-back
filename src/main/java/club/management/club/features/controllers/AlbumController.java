package club.management.club.features.controllers;

import club.management.club.features.dto.responses.AlbumDTO;
import club.management.club.features.entities.Album;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Image;
import club.management.club.features.services.album.AlbumService;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.features.services.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {
    private final ImageService imageService;
    private final ClubService clubService;
    private final AlbumService albumService;
    @PostMapping(path = "/{clubId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addAlbum(
            @RequestParam("images") List<MultipartFile> images,
            @PathVariable String clubId,
            String location,
            String title,
            LocalDate date
    ) throws IOException {
        Club club = clubService.findById(clubId);
        List<Image> savedImages = new ArrayList<>();
        for (MultipartFile file : images){
            Image savedImage = imageService.saveAlbumImage(file);
            savedImages.add(savedImage);
        }
        Album album = Album.builder()
                .albumImages(savedImages)
                .date(date)
                .title(title)
                .location(location)
                .club(club)
                .build();
        albumService.save(album);
    }
    @GetMapping("/{clubId}")
    public List<AlbumDTO> getAlbumsRelatedToClub(@PathVariable String clubId){
        return albumService.getClubAlbums(clubId)
            .stream().map(
                album -> AlbumDTO.builder()
                .albumId(album.getAlbumId())
                .imagesUrl(album.getAlbumImages().stream().map(Image::getId).toList())
                .location(album.getLocation())
                .date(album.getDate())
                .title(album.getTitle())
                .build()
            ).toList();
    }

    @GetMapping("/images/{albumId}")
    public List<byte[]> getAlbumImages(@PathVariable String albumId){
        Album album = albumService.findById(albumId);
        List<Image> images = album.getAlbumImages();
        return  images.stream().map(Image::getData).toList();
    }
}

