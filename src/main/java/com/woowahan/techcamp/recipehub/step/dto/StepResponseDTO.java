package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StepResponseDTO {

    private Long id;

    private Recipe recipe;
    private String name;
    private List<String> content;

    private User writer;
    private String imgUrl;

    private List<Ingredient> ingredients;

    private Long sequence;
    private boolean closed;

    private List<StepResponseDTO> offers;
    private Step target;
    private OfferType offerType;
    private boolean rejected;

    @Builder
    public StepResponseDTO(Long id, Recipe recipe, String name, List<String> content,
                           User writer, String imgUrl, List<Ingredient> ingredients, Long sequence,
                           boolean closed, List<StepResponseDTO> offers, Step target, OfferType offerType, boolean rejected) {
        this.id = id;
        this.recipe = recipe;
        this.name = name;
        this.content = content;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
        this.sequence = sequence;
        this.closed = closed;
        this.offers = offers;
        this.target = target;
        this.offerType = offerType;
        this.rejected = rejected;
    }

    public static StepResponseDTO from(AbstractStep step) {
        return (step instanceof Step) ? fromStep((Step) step) : fromStepOffer((StepOffer) step);
    }

    private static StepResponseDTO fromStep(Step step) {
        List<StepOffer> offers = step.getOffers();
        StepResponseDTOBuilder builder = StepResponseDTO.builder()
                .id(step.getId())
                .name(step.getName())
                .closed(step.isClosed())
                .content(step.getContent())
                .imgUrl(step.getImgUrl())
                .ingredients(step.getIngredients())
                .sequence(step.getSequence())
                .writer(step.getWriter())
                .recipe(step.getRecipe());

        if (offers != null) {
            builder.offers(offers.stream().map(StepResponseDTO::from).collect(Collectors.toList()));
        }

        return builder.build();
    }

    private static StepResponseDTO fromStepOffer(StepOffer offer) {
        return StepResponseDTO.builder()
                .id(offer.getId())
                .name(offer.getName())
                .content(offer.getContent())
                .imgUrl(offer.getImgUrl())
                .ingredients(offer.getIngredients())
                .writer(offer.getWriter())
                .recipe(offer.getRecipe())
                .target(offer.getTarget())
                .offerType(offer.getOfferType())
                .rejected(offer.isRejected())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResponseDTO that = (StepResponseDTO) o;
        return closed == that.closed &&
                Objects.equals(id, that.id) &&
                Objects.equals(recipe, that.recipe) &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content) &&
                Objects.equals(writer, that.writer) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(ingredients, that.ingredients) &&
                Objects.equals(sequence, that.sequence);
    }

    public String getOfferType() {
        if (offerType == null) {
            return null;
        }
        return offerType.toString();
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, recipe, name, content, writer, imgUrl, ingredients, sequence, closed);
    }
}
