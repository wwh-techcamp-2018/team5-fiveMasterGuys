package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StepOfferServiceTest {

    @InjectMocks
    private StepOfferService userRecipeStepService;

    @Mock
    private StepOfferRepository stepOfferRepository;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private MessageSourceAccessor messageSourceAccessor;

    private User user;
    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        user = User.builder().build();
        recipe = Recipe.builder().build();
    }

    @Test
    public void createNullTarget() {
        // given
        String name = "핏짜";
        StepCreationDTO dto = StepCreationDTO.builder()
                .name(name).build();

        StepOffer saved = StepOffer.from(user, dto, recipe, null, OfferType.APPEND);
        when(stepOfferRepository.save(saved)).thenReturn(saved);

        // when
        StepOffer stepOffer = userRecipeStepService.create(user, dto, recipe);

        // then
        assertThat(stepOffer.getTarget()).isNull();
        assertThat(stepOffer.getName()).isEqualTo(name);
        assertThat(stepOffer.getOfferType()).isEqualTo(OfferType.APPEND);
    }


    @Test
    public void create() {
        // given
        String name = "핏짜";

        Step targetStep = Step.builder().build();
        StepCreationDTO dto = StepCreationDTO.builder()
                .name(name).build();

        StepOffer saved = StepOffer.from(user, dto, recipe, targetStep, OfferType.APPEND);
        when(stepOfferRepository.save(saved)).thenReturn(saved);

        // when
        StepOffer stepOffer = userRecipeStepService.create(user, dto, recipe);

        // then
        assertThat(stepOffer.getName()).isEqualTo(name);
        assertThat(stepOffer.getTarget()).isEqualTo(targetStep);
        assertThat(stepOffer.getOfferType()).isEqualTo(OfferType.APPEND);
    }

    @Test
    public void modify() throws Exception {
        // given
        String name = "핏짜";

        Step targetStep = Step.builder().closed(false).build();
        StepCreationDTO dto = StepCreationDTO.builder()
                .name(name).build();

        StepOffer saved = StepOffer.from(user, dto, recipe, targetStep, OfferType.MODIFY);

        when(stepRepository.findById(1L)).thenReturn(Optional.of(targetStep));
        when(stepOfferRepository.save(saved)).thenReturn(saved);

        // when
        StepOffer stepOffer = userRecipeStepService.modify(user, 1L, dto, recipe);

        // then
        assertThat(stepOffer.getName()).isEqualTo(name);
        assertThat(stepOffer.getTarget()).isEqualTo(targetStep);
        assertThat(stepOffer.getOfferType()).isEqualTo(OfferType.MODIFY);
    }


    @Test(expected = BadRequestException.class)
    public void modifyClosedStep() throws Exception {
        // given
        String name = "핏짜";

        Step targetStep = Step.builder().closed(true).build();
        StepCreationDTO dto = StepCreationDTO.builder()
                .name(name).build();


        when(stepRepository.findById(1L)).thenReturn(Optional.of(targetStep));

        // when
        userRecipeStepService.modify(user, 1L, dto, recipe);
    }
}
