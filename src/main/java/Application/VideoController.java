package Application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/api/videos")
public class VideoController {

	private final VideoService videoService;

	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping
	public List<Video> getAllVideos() {
		return videoService.getAllVideos();
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty.");
		}

		try {
			Video video = new Video();
			video.setFilename(file.getOriginalFilename());
			video.setDescription(description);
			video.setVideoData(file.getBytes()); // Store the video data as bytes
			video.setCreatedAt(LocalDateTime.now());
			videoService.saveVideo(video);
			return ResponseEntity.ok("File uploaded successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to upload file.");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getVideo(@PathVariable Long id) {
		return videoService.getVideoById(id).map(video -> ResponseEntity.ok().header("Content-Type", "video/mp4") // Adjust
																													// content
																													// type
																													// as
																													// needed
				.body(video.getVideoData())).orElse(ResponseEntity.notFound().build());
	}
}
