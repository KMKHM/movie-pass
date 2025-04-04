package kr.co.moviepassservice.repository.movie;

import kr.co.moviepassservice.domain.movie.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
