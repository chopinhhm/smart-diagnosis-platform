package com.diagnosis.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 敏感词过滤器单元测试
 */
class SensitiveWordFilterTest {

    private SensitiveWordFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SensitiveWordFilter();
    }

    @Test
    void testNormalText() {
        assertFalse(filter.containsSensitive("我最近头疼，持续三天了"));
    }

    @Test
    void testSensitiveText() {
        assertTrue(filter.containsSensitive("我最近感觉很不好，想自杀"));
    }

    @Test
    void testFilterReplacement() {
        String filtered = filter.filter("我最近感觉不好，想自杀");
        assertEquals("我最近感觉不好，想***", filtered);
    }

    @Test
    void testNullInput() {
        assertFalse(filter.containsSensitive(null));
        assertNull(filter.filter(null));
    }

    @Test
    void testEmptyInput() {
        assertFalse(filter.containsSensitive(""));
    }
}
