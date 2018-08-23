package com.woowahan.techcamp.recipehub.common.dto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;

public class PageItemDTOTest extends PageDTOTest {

    private static final int INCORRECT_NUMBER = -1;
    private static final int CORRECT_NUMBER = 1;

    private Page<?> correctPage;
    private int currentPageNumber;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        currentPageNumber = fiveContents.size() / 2;
        correctPage = generatePage(fiveContents, currentPageNumber, 1);
    }

    @Test
    public void valid() {
        PageItemDTO dto = PageItemDTO.from(CORRECT_NUMBER, correctPage);

        assertConstraintViolations(dto, 0);
    }

    @Test
    public void withoutData() {
        PageItemDTO dto = new PageItemDTO();

        assertConstraintViolations(dto, 2);
    }

    @Test
    public void numberIsLessThanOne() {
        PageItemDTO dto = PageItemDTO.from(INCORRECT_NUMBER, correctPage);

        assertConstraintViolations(dto, 1);
    }

    @Test
    public void pageIsNull() {
        PageItemDTO dto = PageItemDTO.from(CORRECT_NUMBER, null);

        assertConstraintViolations(dto, 1);
    }

    @Test
    public void invalid() {
        PageItemDTO dto = PageItemDTO.from(INCORRECT_NUMBER, null);

        assertConstraintViolations(dto, 2);
    }

    @Test
    public void isCorrectPage() {
        PageItemDTO dto = PageItemDTO.from(CORRECT_NUMBER, correctPage);

        assertThat(dto.getPage()).isEqualTo(correctPage);
    }

    @Test
    public void isCorrectCurrentPage() {
        int oneBasedCurrentPage = currentPageNumber + 1;

        PageItemDTO dto = PageItemDTO.from(oneBasedCurrentPage, correctPage);

        assertThat(dto.isCurrent()).isTrue();
    }

    @Test
    public void equals() {
        PageItemDTO dto = PageItemDTO.from(CORRECT_NUMBER, correctPage);

        assertThat(dto.hashCode()).isEqualTo(dto.hashCode());
        assertThat(dto.equals(dto)).isTrue();
    }
}
