package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;

    @OneToMany(mappedBy = "steps")
    private List<RecipeStep> steps;

    private String imgUrl;

    @Builder
    public Recipe(Category category, User owner, String name, boolean completed, Date createdAt, Date updatedAt, List<RecipeStep> steps, String imgUrl) {
        this.category = category;
        this.owner = owner;
        this.name = name;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.steps = steps;
        this.imgUrl = imgUrl;
    }
}
