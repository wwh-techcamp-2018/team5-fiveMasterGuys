package com.woowahan.techcamp.recipehub.recipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.common.domain.AbstractEntity;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Recipe extends AbstractEntity {

    public static final String DEFAULT_RECIPE_IMAGE_URL = "/img/recipe-default.png";

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean completed;

    @OneToMany(mappedBy = "recipe")
    @OrderBy("sequence ASC")
    @JsonIgnore
    @Where(clause = "type='Step'")
    private List<Step> recipeSteps;

    @Column(nullable = false)
    private String imgUrl;

    @Builder
    public Recipe(Category category, User owner, String name, boolean completed, List<Step> recipeSteps, String imgUrl) {
        this.category = category;
        this.owner = owner;
        this.name = name;
        this.completed = completed;
        this.recipeSteps = recipeSteps;
        this.imgUrl = (imgUrl == null) ? DEFAULT_RECIPE_IMAGE_URL : imgUrl;
    }


    public boolean isOwner(User user) {
        return user.equals(this.owner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        return id != null ? id.equals(recipe.id) : recipe.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void complete() {
        completed = true;
    }
}
