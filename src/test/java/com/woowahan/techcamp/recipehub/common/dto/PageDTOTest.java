package com.woowahan.techcamp.recipehub.common.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Before;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

public abstract class PageDTOTest extends ValidationTest {

    protected List<String> fiveContents;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        fiveContents = Arrays.asList("a", "b", "c", "d", "e");
    }

    protected static <T> Page<T> generatePage(List<T> contents, int currentPage, int pageContentSize) {
        return new PageImpl<>(contents,
                PageRequest.of(currentPage, pageContentSize),
                contents.size());
    }
}
