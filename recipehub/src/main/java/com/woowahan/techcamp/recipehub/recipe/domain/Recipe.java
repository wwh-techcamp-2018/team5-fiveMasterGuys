package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.user.domain.User;

import java.util.Date;
import java.util.List;

public class Recipe {
    private Long id;
    private Category category;
    private User owner;
    private String name;
    private boolean completed;
    private Date createdAt;
    private Date updatedAt;
    private List<RecipeStep> steps;
    private String imgUrl;
}
