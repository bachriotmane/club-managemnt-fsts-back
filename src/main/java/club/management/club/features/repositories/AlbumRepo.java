package club.management.club.features.repositories;

import club.management.club.features.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepo extends JpaRepository<Album,String> {
    List<Album> findByClub_Id(String clubId);
}
