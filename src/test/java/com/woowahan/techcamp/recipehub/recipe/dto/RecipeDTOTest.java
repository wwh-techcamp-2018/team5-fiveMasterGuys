package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RecipeDTOTest extends ValidationTest {

    @Test
    public void basic() {
        RecipeDTO dto = RecipeDTO.builder()
                .category(new Category())
                .name("초코치킨").build();

        assertConstraintViolations(dto, 0);
    }

    @Test
    public void wihtoutData() {
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
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").category(null).build();
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
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").category(new Category()).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withNullImage() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").category(new Category()).imgUrl(null).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void toEntity() {
        String name = "양념치킨";
        Category category = new Category();
        RecipeDTO dto = RecipeDTO.builder().name(name).category(category).build();
        Recipe entity = dto.toEntity();
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getImgUrl()).isEqualTo(null);
    }
}