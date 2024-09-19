package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdoptionsService {

    @Autowired
    private AdoptionsRepository adoptionsRepository;

    public List<Adoptions> getAvailableAdoptions() {
        return adoptionsRepository.findByStatus(true); // true indicates available
    }
}
