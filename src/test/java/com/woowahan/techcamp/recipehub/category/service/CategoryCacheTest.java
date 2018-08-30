package com.woowahan.techcamp.recipehub.category.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.config.CachingConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryCacheTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CachingConfig cachingConfig;

    @SpyBean
    private CategoryService spyService;

    @Before
    public void setUp() throws Exception {
        categoryRepository.save(Category.builder().title("category A").build());
        categoryRepository.save(Category.builder().title("category B").build());
    }

    @Test
    public void getAllCacheHitTest() {
        spyService.findAll();
        spyService.findAll();
        verify(spyService, times(1)).findAll();
    }

    @Test
    public void getAllCacheEvictTest() {
        spyService.findAll();
        cachingConfig.evictCategoriesCache();
        spyService.findAll();
        verify(spyService, times(2)).findAll();
    }


    @After
    public void tearDown() throws Exception {
        cachingConfig.evictCategoriesCache();
        categoryRepository.deleteAll();
    }
}
