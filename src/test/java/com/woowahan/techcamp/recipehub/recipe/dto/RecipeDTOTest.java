package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RecipeDTOTest extends ValidationTest {

    @Test
    public void basic() {
        RecipeDTO dto = RecipeDTO.builder()
                .categoryId(1L)
                .name("초코치킨").build();

        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withoutData() {
        RecipeDTO dto = RecipeDTO.builder().build();
        assertConstraintViolations(dto, 3);
    }

    @Test
    public void withoutCategory() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void withNullCategory() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").categoryId(null).build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void withBlankName() {
        RecipeDTO dto = RecipeDTO.builder().name("").build();
        assertConstraintViolations(dto, 2);

        dto = RecipeDTO.builder().name(" ").build();
        assertConstraintViolations(dto, 2);
    }

    @Test
    public void withoutImage() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").categoryId(1L).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withNullImage() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").categoryId(1L).imgUrl(null).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void toEntityImgUrlIsNull() {
        String name = "양념치킨";
        Category category = Category.builder().build();
        User user = User.builder().name("Ryun").email("ryuneeee@gmail.com").build();
        RecipeDTO dto = RecipeDTO.builder().name(name).categoryId(1L).build();
        Recipe entity = dto.toEntity(user, category);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getImgUrl()).isEqualTo(Recipe.DEFAULT_RECIPE_IMAGE_URL);
    }

    @Test
    public void toEntityNormally() {
        final String name = "양념치킨";
        final String imgUrl = "imgUrl";
        Category category = Category.builder().build();
        User user = User.builder().name("Ryun").email("ryuneeee@gmail.com").build();

        RecipeDTO dto = RecipeDTO.builder().name(name).categoryId(1L).imgUrl(imgUrl).build();
        Recipe entity = dto.toEntity(user, category);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getImgUrl()).isEqualTo(imgUrl);
    }
}