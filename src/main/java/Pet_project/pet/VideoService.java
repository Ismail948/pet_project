package Pet_project.pet;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
    
    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }
    public void saveSmallVideo() {
        Video smallVideo = new Video();
        smallVideo.setFilename("small_test.mp4");
        smallVideo.setDescription("Small test video");
        smallVideo.setCreatedAt(LocalDateTime.now());
        smallVideo.setVideoData(new byte[]{0}); // Small dummy data

        videoRepository.save(smallVideo);
    }

    
    public void saveVideo(Video video) {
    	 if (video.getVideoData() != null) {
    	        System.out.println("Video data size: " + video.getVideoData().length);
    	    }
        videoRepository.save(video);
    }
}
