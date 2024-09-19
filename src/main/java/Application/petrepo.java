package Application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface petrepo extends JpaRepository<pettype, Long> {

}
