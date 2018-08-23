package com.woowahan.techcamp.recipehub.category.domain;

import com.woowahan.techcamp.recipehub.common.domain.AbstractEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class Category extends AbstractEntity {

    private String title;

    @Builder
    public Category(String title) {
        this.title = title;
    }
}
