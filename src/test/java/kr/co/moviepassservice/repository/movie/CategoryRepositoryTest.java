package kr.co.moviepassservice.repository.movie;

import kr.co.moviepassservice.domain.movie.AgeRating;
import kr.co.moviepassservice.domain.movie.Category;
import kr.co.moviepassservice.domain.movie.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("영화 카테고리 저장: 카테고리가 정상적으로 저장되는지 확인한다")
    void save_Category_ShouldBeSavedCorrectly() {
        // Given
        String categoryName = "Action";
        Category category = createCategory(categoryName);

        // When
        Category savedCategory = categoryRepository.save(category);

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("카테고리 조회: 저장된 카테고리를 ID로 조회할 수 있다")
    void findById_WithExistingId_ShouldReturnCategory() {
        // Given
        Category category = createCategory("Thriller");
        Category savedCategory = categoryRepository.save(category);

        // When
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        // Then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Thriller");
    }

    @Test
    @DisplayName("카테고리-영화 매핑: 카테고리와 영화가 정상적으로 연결된다")
    void movieCategory_Mapping_ShouldBeCorrect() {
        // Given
        // 카테고리 생성 및 저장
        Category action = createCategory("액션");
        Category sf = createCategory("SF");
        categoryRepository.saveAll(List.of(action, sf));

        // 영화 생성
        Movie movie = Movie.builder()
                .title("매트릭스")
                .description("가상현실과 실제 현실의 경계에 관한 영화")
                .director("워쇼스키 자매")
                .runtime(136)
                .releaseDate(LocalDate.of(1999, 5, 15))
                .posterUrl("https://example.com/matrix.jpg")
                .ageRating(AgeRating.FIFTEEN)
                .isScreening(true)
                .build();

        // 영화에 카테고리 추가
        movie.addCategory(action);
        movie.addCategory(sf);
        
        // 영화 저장
        Movie savedMovie = movieRepository.save(movie);

        // When
        // 저장된 영화 다시 조회
        Movie foundMovie = movieRepository.findById(savedMovie.getId()).orElseThrow();
        
        // Then
        // 영화의 카테고리 확인
        assertThat(foundMovie.getCategories()).hasSize(2);
        assertThat(foundMovie.getCategories()).extracting("name")
                .containsExactlyInAnyOrder("액션", "SF");
        
        // 카테고리에서 영화 확인
        List<Category> categories = categoryRepository.findAll();
        Category foundAction = categories.stream()
                .filter(c -> c.getName().equals("액션"))
                .findFirst()
                .orElseThrow();
        
        assertThat(foundAction.getMovies()).hasSize(1);
        assertThat(foundAction.getMovies().get(0).getTitle()).isEqualTo("매트릭스");
    }

    @Test
    @DisplayName("카테고리 삭제: 카테고리가 정상적으로 삭제되는지 확인한다")
    void delete_Category_ShouldBeDeletedCorrectly() {
        // Given
        Category category = createCategory("Comedy");
        Category savedCategory = categoryRepository.save(category);
        Long categoryId = savedCategory.getId();

        // When
        categoryRepository.delete(savedCategory);

        // Then
        Optional<Category> deletedCategory = categoryRepository.findById(categoryId);
        assertThat(deletedCategory).isEmpty();
    }

    @Test
    @DisplayName("중복 카테고리명: 동일한 이름의 카테고리는 저장되지 않아야 한다")
    void save_DuplicateCategoryName_ShouldFail() {
        // Given
        String duplicateName = "로맨스";
        Category firstCategory = createCategory(duplicateName);
        categoryRepository.save(firstCategory);

        Category secondCategory = createCategory(duplicateName);

        // When & Then
        try {
            categoryRepository.save(secondCategory);
            categoryRepository.flush(); // 즉시 DB에 반영하여 제약 조건 위반 확인
            
            assertThat(false).isTrue(); // 여기까지 도달하면 테스트 실패
        } catch (Exception e) {
            // 제약 조건 위반 예외가 발생해야 함
            assertThat(e).isNotNull();
        }
    }

    private static Category createCategory(String categoryName) {
        return Category.builder()
                .name(categoryName)
                .build();
    }
}
