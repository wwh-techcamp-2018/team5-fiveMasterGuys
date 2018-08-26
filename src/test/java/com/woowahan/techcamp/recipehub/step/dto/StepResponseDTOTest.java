package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StepResponseDTOTest {

    private Step.StepBuilder recipeStepBuilder;
    private StepOffer.StepOfferBuilder recipeStepOfferBuilder;

    @Before
    public void setUp() throws Exception {
        List<String> expectedStepContents = Arrays.asList("식빵을 토스터기에 굽는다", "잼을 바른다");
        recipeStepBuilder = Step.builder()
                .id(1L)
                .name("잼을 바른 토스트")
                .closed(false)
                .content(expectedStepContents)
                .imgUrl("http://imgurl.com/static/img/image.jpg")
                .ingredients(new ArrayList<>())
                .sequence(1L)
                .writer(User.builder().id(1L).build())
                .offers(Arrays.asList(new StepOffer(), new StepOffer()))
                .recipe(Recipe.builder().build());

        recipeStepOfferBuilder = StepOffer.builder()
                .id(1L)
                .name("잼을 바른 토스트")
                .content(expectedStepContents)
                .imgUrl("http://imgurl.com/static/img/image.jpg")
                .ingredients(new ArrayList<>())
                .writer(User.builder().id(1L).build())
                .target(Step.builder().build())
                .offerType(OfferType.APPEND)
                .rejected(false)
                .recipe(Recipe.builder().build());
    }

    @Test
    public void basic() throws Exception {
        assertThat(new StepResponseDTO()).isInstanceOf(StepResponseDTO.class);
    }

    @Test
    public void from() throws Exception {
        StepResponseDTO stepDTO = StepResponseDTO.from(recipeStepBuilder.build());
        assertThat(stepDTO).hasFieldOrProperty("sequence");
        assertThat(stepDTO).hasFieldOrProperty("offers");
        assertThat(stepDTO).hasFieldOrProperty("closed");
        assertThat(stepDTO).hasNoNullFieldsOrPropertiesExcept("target", "offerType", "rejected");

        StepResponseDTO offerDTO = StepResponseDTO.from(recipeStepOfferBuilder.build());
        assertThat(offerDTO).hasFieldOrProperty("target");
        assertThat(offerDTO).hasFieldOrProperty("offerType");
        assertThat(offerDTO).hasFieldOrProperty("rejected");
        assertThat(offerDTO).hasNoNullFieldsOrPropertiesExcept("sequence", "offers", "closed");
    }

    @Test
    public void fromStep() {
        Step recipeStep = recipeStepBuilder.build();
        StepResponseDTO stepResponseDTO = StepResponseDTO.from(recipeStep);
        assertAbstractRecipeStepDtoEqualToRecipe(stepResponseDTO, recipeStep);

        assertThat(stepResponseDTO.getSequence()).isEqualTo(recipeStep.getSequence());
        assertThat(stepResponseDTO.getOffers()).isEqualTo(recipeStep.getOffers().stream().map(StepResponseDTO::from).collect(Collectors.toList()));
        assertThat(stepResponseDTO.isClosed()).isEqualTo(recipeStep.isClosed());

    }

    @Test
    public void fromStepOffer() throws Exception {
        StepOffer offer = recipeStepOfferBuilder.content(new ArrayList<>()).build();
        StepResponseDTO stepResponseDTO = StepResponseDTO.from(offer);

        assertAbstractRecipeStepDtoEqualToRecipe(stepResponseDTO, offer);
        assertThat(stepResponseDTO.isRejected()).isEqualTo(offer.isRejected());
        assertThat(stepResponseDTO.getOfferType()).isEqualTo(offer.getOfferType());
        assertThat(stepResponseDTO.getTarget()).isEqualTo(offer.getTarget());
    }

    @Test
    public void fromEmptyContents() {
        Step recipeStep = recipeStepBuilder.content(new ArrayList<>()).build();
        StepResponseDTO stepResponseDTO = StepResponseDTO.from(recipeStep);

        assertAbstractRecipeStepDtoEqualToRecipe(stepResponseDTO, recipeStep);
        assertThat(stepResponseDTO.isClosed()).isEqualTo(recipeStep.isClosed());
        assertThat(stepResponseDTO.getSequence()).isEqualTo(recipeStep.getSequence());
    }

    private void assertAbstractRecipeStepDtoEqualToRecipe(StepResponseDTO stepResponseDTO, AbstractStep recipeStep) {
        assertThat(stepResponseDTO.getContent()).isEqualTo(recipeStep.getContent());
        assertThat(stepResponseDTO.getId()).isEqualTo(recipeStep.getId());
        assertThat(stepResponseDTO.getName()).isEqualTo(recipeStep.getName());
        assertThat(stepResponseDTO.getImgUrl()).isEqualTo(recipeStep.getImgUrl());
        assertThat(stepResponseDTO.getIngredients()).isEqualTo(recipeStep.getIngredients());
        assertThat(stepResponseDTO.getWriter()).isEqualTo(recipeStep.getWriter());
        assertThat(stepResponseDTO.getRecipe()).isEqualTo(recipeStep.getRecipe());
    }
}
