package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.user.domain.User;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public abstract class AbstractRecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;
    private String name;
    private String content;

    @ManyToOne
    private User writer;
    private String imgUrl;

    @OneToMany
    private List<Ingredient> ingredients;
}
