package Pet_project.pet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import Tables.Video;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class Startingcontroller {

	int i;
	@Autowired
	private final VideoService videoService;

	public Startingcontroller(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/")
	public String indexpage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		return "index";
	}

	@GetMapping("/login")
	public String login(HttpServletResponse response) {
		return "login";
	}

	@GetMapping("/successreg")
	public String userregsuccess() {
		return "successregister";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		setNoCacheHeaders(response);

		// Invalidate session and clear cookies
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		clearCookies(request, response);
		return "redirect:/login";
	}

	@GetMapping("/explore")
	public String explorePage(Model model) {
		List<Video> videos = videoService.getAllVideos();
		model.addAttribute("videos", videos);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		return "explore";
	}

	@GetMapping("/videos/{id}")
	public ResponseEntity<ByteArrayResource> getVideo(@PathVariable Long id) {
		return videoService.getVideoById(id).map(video -> {
			ByteArrayResource resource = new ByteArrayResource(video.getVideoData());
			return ResponseEntity.ok().contentType(MediaType.valueOf("video/mp4")) // Adjust content type based on video
																					// format
					.contentLength(video.getVideoData().length).body(resource);
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/upload")
	public String uploadvideo() {
		return "upload";
	}

	private void setNoCacheHeaders(HttpServletResponse response) {
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		response.setHeader(HttpHeaders.PRAGMA, "no-cache");
		response.setDateHeader(HttpHeaders.EXPIRES, 0);
	}

	private void clearCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equals(cookie.getName())) {
					cookie.setMaxAge(0);
					cookie.setValue(null);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
	}
}
