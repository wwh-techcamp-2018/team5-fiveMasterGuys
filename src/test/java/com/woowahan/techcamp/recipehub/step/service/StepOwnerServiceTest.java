package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTOTest;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class StepOwnerServiceTest {

    @InjectMocks
    private StepOwnerService service;

    @Mock
    private StepOfferRepository stepOfferRepository;

    @Mock
    private StepRepository stepRepository;
    private StepCreationDTO.StepCreationDTOBuilder dtoBuilder;
    private Step recipeStep;
    private Recipe recipe;
    private User owner;

    @Before
    public void setUp() throws Exception {

        dtoBuilder = StepCreationDTO.builder()
                .name("asdf")
                .content(Arrays.asList("a", "b"))
                .ingredients(null)
                .previousStepId(null)
                .imgUrl("/static/img/image.jpg");
        owner = User.builder().id(1L).build();
        recipe = Recipe.builder().owner(owner).build();
    }

    @Test
    public void createFirstStep() {
        StepCreationDTO dto = dtoBuilder.build();

        when(stepRepository.save(any())).then(returnsFirstArg());

        Step resultRecipeStep = service.create(
                owner,
                dto,
                recipe);


        verify(stepRepository).increaseSequenceGte(recipe, 1L);
        assertThat(dto).isEqualToComparingOnlyGivenFields(resultRecipeStep, "name", "content", "ingredients", "imgUrl");

        assertThat(resultRecipeStep.getRecipe()).isEqualTo(recipe);
        assertThat(resultRecipeStep.getWriter()).isEqualTo(owner);
        assertThat(resultRecipeStep.getSequence()).isEqualTo(1L);
        assertThat(resultRecipeStep.isClosed()).isFalse();
    }

    @Test
    public void createWithPreviousStep() {
        Long previousStepId = 1L;
        StepCreationDTO dto = dtoBuilder.previousStepId(previousStepId).build();

        Step previousStep = Step.builder().id(previousStepId).sequence(3L).build();

        when(stepRepository.findById(previousStepId)).thenReturn(Optional.of(previousStep));

        when(stepRepository.save(any())).then(returnsFirstArg());
        Step resultRecipeStep = service.create(
                owner,
                dto,
                recipe);

        verify(stepRepository).increaseSequenceGte(recipe, previousStep.getSequence() + 1);

        assertThat(dto).isEqualToComparingOnlyGivenFields(resultRecipeStep, "name", "content", "ingredients", "imgUrl");

        assertThat(resultRecipeStep.getRecipe()).isEqualTo(recipe);
        assertThat(resultRecipeStep.getWriter()).isEqualTo(owner);
        assertThat(resultRecipeStep.getSequence()).isEqualTo(previousStep.getSequence() + 1);
        assertThat(resultRecipeStep.isClosed()).isFalse();
    }


    @Test
    public void modify() {
        Long previousStepId = 1L;
        StepCreationDTO dto = dtoBuilder.previousStepId(previousStepId).build();

        Step previousStep = Step.builder().id(previousStepId)
                .closed(false)
                .sequence(3L)
                .build();

        when(stepRepository.findById(previousStepId)).thenReturn(Optional.of(previousStep));

        when(stepRepository.save(any())).then(returnsFirstArg());

        Step resultStep = service.modify(owner, previousStepId, dto, recipe);

        StepCreationDTOTest.assertDtoEqualToStep(dto, resultStep);
        assertThat(resultStep.isClosed()).isFalse();
        assertThat(resultStep.getSequence()).isEqualTo(previousStep.getSequence());
        assertThat(previousStep.isClosed()).isTrue();

        verify(stepOfferRepository).rejectModifyingOfferByTarget(previousStep);
        verify(stepOfferRepository).changeAppendOffersTarget(previousStep, resultStep);
    }


    @Test(expected = EntityNotFoundException.class)
    public void modifyNotExistStep() {
        StepCreationDTO dto = dtoBuilder.previousStepId(1L).build();
        service.modify(owner, 1L, dto, recipe);
    }
}