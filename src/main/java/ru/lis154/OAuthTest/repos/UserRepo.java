package ru.lis154.OAuthTest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.lis154.OAuthTest.entities.User;

@Service
@Repository
public interface UserRepo extends JpaRepository<User, Long>
{
    User findByUsername(String email);
    User findByName(String name);
    User findByGoogleUsername(String googleUsername);
    User findByGoogleName(String googleName);
}