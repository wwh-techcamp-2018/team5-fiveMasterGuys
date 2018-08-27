package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.dto.StepResponseDTO;
import com.woowahan.techcamp.recipehub.step.repository.AbstractStepRepository;
import com.woowahan.techcamp.recipehub.step.service.StepOfferService;
import com.woowahan.techcamp.recipehub.step.service.StepOwnerService;
import com.woowahan.techcamp.recipehub.step.service.StepServiceProvider;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/recipes/{recipeId}/steps")
public class StepRestController {

    @Autowired
    private StepServiceProvider provider;

    @Autowired
    private AbstractStepRepository abstractStepRepository;

    @Autowired
    private StepOwnerService ownerService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/{stepId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<StepResponseDTO> get(@PathVariable("recipeId") long recipeId, @PathVariable("stepId") long stepId) {
        AbstractStep step = abstractStepRepository.findById(stepId).orElseThrow(EntityNotFoundException::new);
        return RestResponse.success(StepResponseDTO.from(step));
    }

    @AuthRequired
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<AbstractStep> create(@PathVariable("recipeId") long recipeId, User user, @Valid @RequestBody StepCreationDTO dto) {
        Recipe recipe = recipeService.findById(recipeId);
        return RestResponse.success(
                provider.getService(recipe, user).create(user, dto, recipe)
        );
    }

    @AuthRequired
    @PutMapping("/{stepId}")
    public RestResponse<AbstractStep> modify(@PathVariable("recipeId") long recipeId,
                                             @PathVariable("stepId") long stepId,
                                             User user,
                                             @Valid @RequestBody StepCreationDTO dto) {
        Recipe recipe = recipeService.findById(recipeId);
        return RestResponse.success(
                provider.getService(recipe, user).modify(user, stepId, dto, recipe)
        );
    }

    @AuthRequired
    @GetMapping("/{offerId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<Step> approveAppendOffer(@PathVariable("recipeId") long recipeId, @PathVariable("offerId") long offerId, User user) {
        Recipe recipe = recipeService.findById(recipeId);

        if (!recipe.isOwner(user)) {
            throw new UnauthorizedException();
        }

        return RestResponse.success(
                ownerService.approveAppendOffer(recipe, offerId, user)
        );
    }
}
