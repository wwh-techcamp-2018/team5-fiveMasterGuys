package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RecipeCreationDTOTest extends ValidationTest {

    @Test
    public void basic() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder()
                .category(new Category())
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
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").category(null).build();
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
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").category(new Category()).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withNullImage() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").category(new Category()).imgUrl(null).build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void toEntity() {
        String name = "양념치킨";
        Category category = new Category();
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name(name).category(category).build();
        Recipe entity = dto.toEntity();
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getCategory()).isEqualTo(category);
        assertThat(entity.getImgUrl()).isEqualTo(null);
    }
}