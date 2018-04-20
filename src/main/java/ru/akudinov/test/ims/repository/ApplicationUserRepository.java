package ru.akudinov.test.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akudinov.test.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}