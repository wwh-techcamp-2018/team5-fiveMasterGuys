package com.woowahan.techcamp.recipehub.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PageListDTO {

    public static final int FIRST_PAGE = 1;

    @NotNull
    private List<PageItemDTO> items;

    @NotNull
    private Page<?> page;

    @Min(1)
    private int showingPageSize;

    private PageListDTO(@NotNull List<PageItemDTO> items, @NotNull Page<?> page, @Min(1) int showingPageSize) {
        this.items = items;
        this.page = page;
        this.showingPageSize = showingPageSize;
    }

    public static PageListDTO from(Page<?> page, int showingPageSize) {
        return new PageListDTO(generateItemList(page, showingPageSize), page, showingPageSize);
    }

    public int getPrev() {
        if (page.getNumber() / showingPageSize > 0) {
            return page.getNumber() / showingPageSize * showingPageSize;
        }

        return FIRST_PAGE;
    }

    public int getNext() {
        if ((page.getNumber() / showingPageSize) < (page.getTotalPages() / showingPageSize)) {
            return ((page.getNumber() / showingPageSize) + 1) * showingPageSize + 1;
        }

        return page.getTotalPages();
    }

    static List<PageItemDTO> generateItemList(Page<?> page, int pageSize) {
        if (page == null) {
            return null;
        }

        int start = page.getNumber() / pageSize * pageSize + 1;
        int end = (start + pageSize) < (page.getTotalPages() + 1) ?
                (start + pageSize) :
                (page.getTotalPages() + 1);

        List<PageItemDTO> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(PageItemDTO.from(i, page));
        }

        return result;
    }
}
