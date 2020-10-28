package ru.lis154.OAuthTest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.lis154.OAuthTest.entities.Note;

import java.util.List;

@Service
@Repository
public interface NoteRepo extends JpaRepository<Note, Long>
{
    List<Note> findByUserId(Long userId);
}
