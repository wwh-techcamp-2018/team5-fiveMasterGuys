package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RecipeCreationDTOTest extends ValidationTest {

    private static final String RECIPE_DEFAULT_IMAGE_PATH = "/img/recipe-default.png";

    @Test
    public void basic() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder()
                .categoryId(1L)
                .name("초코치킨").build();

        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withoutData() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().build();
        assertConstraintViolations(dto, 3);
    }

    @Test
    public void withoutCategory() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void withNullCategory() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").categoryId(null).build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void withBlankName() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("").build();
        assertConstraintViolations(dto, 2);

        dto = RecipeCreationDTO.builder().name(" ").build();
        assertConstraintViolations(dto, 2);
    }

    @Test
    public void withoutImage() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").categoryId(1L).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withNullImage() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").categoryId(1L).imgUrl(null).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void toEntity() {
        String name = "양념치킨";
        Category category = Category.builder().build();
        User user = User.builder().name("Ryun").email("ryuneeee@gmail.com").build();
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name(name).categoryId(1L).build();
        Recipe entity = dto.toEntity(user, category);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getImgUrl()).isEqualTo(RECIPE_DEFAULT_IMAGE_PATH);
    }
}