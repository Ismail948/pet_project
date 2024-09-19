package Application;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class Startingcontroller {

	@Autowired
	private AdoptionsService adoptionsService;
	@Autowired
	private final VideoService videoService;

	@Autowired
	private petrepo petr;

	public Startingcontroller(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			System.err.println("Username:-"+ user.getUsername());
			model.addAttribute("username", user.getUsername());
			model.addAttribute("password", user.getPassword());
		}

		return "profile";
	}

	@GetMapping("/adopt")
	public String getAvailableAdoptions(@RequestParam(value = "type", required = false) String type, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			model.addAttribute("username", user.getUsername());
		}

		List<Adoptions> availableAdoptions = adoptionsService.getAvailableAdoptions();

		// If type is "all", we don't filter. Otherwise, filter by type.
		if (type != null && !type.equals("all")) {
			availableAdoptions = availableAdoptions.stream()
					.filter(adoption -> adoption.getPetType().equalsIgnoreCase(type)).collect(Collectors.toList());
		}

		// Add the type to the model
		model.addAttribute("type", type == null ? "all" : type); // Default to "all" if type is null
		System.err.println("Again -----" + type);

		model.addAttribute("adoptions", availableAdoptions);
		return "adoptapet";
	}

	@GetMapping("/petPhoto/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getPetPhoto(@PathVariable Long id) {
		Optional<Adoptions> adoption = adoptionsRepository.findById(id);
		if (adoption.isPresent()) {
			byte[] photo = adoption.get().getPetPhoto();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // or the appropriate type
					.body(photo);
		}
		return ResponseEntity.notFound().build();
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

	@GetMapping("/contact")
	public String contact(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		return "contact";
	}

	@GetMapping("/login")
	public String login(HttpServletResponse response) {
		return "login";
	}

	@GetMapping("/successreg")
	public String userregsuccess() {
		return "successregister";
	}

	@Autowired
	private AdoptionsRepository adoptionsRepository;

	@GetMapping("/donor")
	public String showAdoptionForm(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		model.addAttribute("adoption", new Adoptions());
		List<pettype> pet_type = petr.findAll();
		model.addAttribute("types", pet_type);
		return "adoptionForm"; // Name of the HTML file for the adoption form
	}

	@PostMapping("/donor")
	public String submitAdoptionForm(@RequestParam("petPhoto") MultipartFile petPhoto,
			@RequestParam("selectedPetType") String selectedPetType, // Add this line to capture the pet type
			@RequestParam("lane") String lane, @RequestParam("city") String city, @RequestParam("state") String state,
			@RequestParam("pincode") String pincode, @RequestParam("description") String description,
			@RequestParam("phoneNumber") String phoneNumber) {

		if (petPhoto.isEmpty()) {
			System.err.println("No file uploaded");
			return "adoptionForm"; // Redirect back or show an error
		}

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.getPrincipal() instanceof User) {
				User user = (User) authentication.getPrincipal();
				String username = user.getUsername();

				// Convert MultipartFile to byte array
				byte[] petPhotoBytes = petPhoto.getBytes();

				// Create an Adoptions object
				Adoptions adoption = new Adoptions();
				adoption.setPetPhoto(petPhotoBytes);
				adoption.setLane(lane);
				adoption.setCity(city);
				adoption.setState(state);
				adoption.setPincode(pincode);
				adoption.setDescription(description);
				adoption.setStatus(true);
				adoption.setPostedBy(username);
				adoption.setPhoneNumber(phoneNumber);

				// Retrieve the pet type and set it to the adoption object
				adoption.setPetType(selectedPetType);

				// Save the adoption record
				adoptionsRepository.save(adoption);
			}
		} catch (IOException e) {
			e.printStackTrace(); // Log the exception if something goes wrong
			return "adoptionForm"; // Redirect back or show an error
		}

		return "index"; // Redirect after successful submission
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

		// Get the authenticated user's username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}

		return "explore"; // This returns the Thymeleaf template
	}

	@GetMapping("/shopping")
	public String shopping(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		return "shopping";
	}

	@GetMapping("/hospitals")
	public String hospitals(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
		return "hospitals";
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
	public String uploadvideo(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			String username = user.getUsername();
			model.addAttribute("username", username);
		}
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
