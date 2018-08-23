package com.woowahan.techcamp.recipehub.common.dto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import static com.woowahan.techcamp.recipehub.common.dto.PageListDTO.FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class PageListDTOTest extends PageDTOTest {

    private static final int INCORRECT_PAGE_SIZE = -1;
    private static final int CORRECT_PAGE_SIZE = 2;

    private Page<?> correctPage;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        correctPage = generatePage(fiveContents, 2, 4);
    }

    @Test
    public void valid() {
        PageListDTO dto = PageListDTO.from(correctPage, CORRECT_PAGE_SIZE);

        assertConstraintViolations(dto, 0);
    }

    @Test
    public void emptyData() {
        PageListDTO dto = new PageListDTO();

        assertConstraintViolations(dto, 3);
    }

    @Test
    public void pageIsNull() {
        PageListDTO dto = PageListDTO.from(null, CORRECT_PAGE_SIZE);

        assertConstraintViolations(dto, 2);
    }

    @Test
    public void incorrectPageSize() {
        PageListDTO dto = PageListDTO.from(correctPage, INCORRECT_PAGE_SIZE);

        assertConstraintViolations(dto, 1);
    }

    @Test
    public void invalid() {
        PageListDTO dto = PageListDTO.from(null, INCORRECT_PAGE_SIZE);

        assertConstraintViolations(dto, 3);
    }

    @Test
    public void isCorrectPage() {
        PageListDTO dto = PageListDTO.from(correctPage, CORRECT_PAGE_SIZE);

        assertThat(dto.getPage()).isEqualTo(correctPage);
    }

    @Test
    public void isCorrectPageSize() {
        PageListDTO dto = PageListDTO.from(correctPage, CORRECT_PAGE_SIZE);

        assertThat(dto.getShowingPageSize()).isEqualTo(CORRECT_PAGE_SIZE);
    }

    @Test
    public void getPrevPageIfPrevIsExist() {
        int currentPage = fiveContents.size() / 2;
        int showingPageSize = 2;

        Page middlePageWithOneContent = generatePage(fiveContents, currentPage, 1);
        PageListDTO dtoShowingTwoPage = PageListDTO.from(middlePageWithOneContent, showingPageSize);

        assertThat(dtoShowingTwoPage.getPrev())
                .isEqualTo(currentPage - currentPage % showingPageSize);
    }

    @Test
    public void getFirstPageIfPrevIsNotExist() {
        Page firstPageWithOneContent = generatePage(fiveContents, 0, 1);
        PageListDTO dtoShowingTwoPage = PageListDTO.from(firstPageWithOneContent, 2);

        assertThat(dtoShowingTwoPage.getPrev()).isEqualTo(FIRST_PAGE);
    }

    @Test
    public void getNextPageIfNextIsExist() {
        int currentPage = fiveContents.size() / 2;
        int showingPageSize = 2;

        Page middlePageWithOneContent = generatePage(fiveContents, currentPage, 1);
        PageListDTO dtoShowingTwoPage = PageListDTO.from(middlePageWithOneContent, showingPageSize);

        assertThat(dtoShowingTwoPage.getNext())
                .isEqualTo(currentPage - currentPage % showingPageSize + showingPageSize + 1);
    }

    @Test
    public void getLastPageIfNextIsNotExist() {
        Page lastPageWithOneContent = generatePage(fiveContents, fiveContents.size() - 1, 1);
        PageListDTO dtoShowingTwoPage = PageListDTO.from(lastPageWithOneContent, 2);

        assertThat(dtoShowingTwoPage.getNext()).isEqualTo(fiveContents.size());
    }

    @Test
    public void fromGenerateItemList() {
        int currentPage = fiveContents.size() / 2;
        int pageContentSize = 2;
        Page pageContainsTwoContents = generatePage(fiveContents, currentPage, 2);

        assertThat(PageListDTO.generateItemList(pageContainsTwoContents, pageContentSize))
                .containsExactly(
                        PageItemDTO.from(currentPage + 1, pageContainsTwoContents),
                        PageItemDTO.from(currentPage + 2, pageContainsTwoContents)
                );
    }
}
