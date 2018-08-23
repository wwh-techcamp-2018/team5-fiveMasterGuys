package com.woowahan.techcamp.recipehub.ingredient.domain;

import com.woowahan.techcamp.recipehub.common.domain.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Ingredient extends AbstractEntity {

    private String name;
}
