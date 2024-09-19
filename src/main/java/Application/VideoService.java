package Application;

import org.springframework.stereotype.Service;
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
    
    public Optional<Video> getVideoByFilename(String filename) {
        return videoRepository.findByFilename(filename);
    }
    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }


    
    public void saveVideo(Video video) {
    	 if (video.getVideoData() != null) {
    	        System.out.println("Video data size: " + video.getVideoData().length);
    	    }
        videoRepository.save(video);
    }
}
