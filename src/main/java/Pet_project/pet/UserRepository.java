package Pet_project.pet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Tables.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
//    User findByUsername(String username); // Consider changing this to List<User> if necessary
}
