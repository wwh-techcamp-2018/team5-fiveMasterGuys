package com.woowahan.techcamp.recipehub.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class PageItemDTO {

    @Min(1)
    private int number;

    @NotNull
    private Page<?> page;

    private PageItemDTO(@Min(1) int number, @NotNull Page<?> page) {
        this.number = number;
        this.page = page;
    }

    public static PageItemDTO from(int number, Page<?> page) {
        return new PageItemDTO(number, page);
    }

    public boolean isCurrent() {
        return number == (page.getNumber() + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageItemDTO that = (PageItemDTO) o;
        return number == that.number &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, page);
    }
}
