package com.climbing.config;

import com.climbing.constant.SortType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SortTypeRequestConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String sortType) {
        return SortType.valueOf(sortType);
    }
}
