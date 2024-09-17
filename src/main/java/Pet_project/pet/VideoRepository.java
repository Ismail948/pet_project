package Pet_project.pet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Tables.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
	Optional<Video> findByFilename(String filename);
}
