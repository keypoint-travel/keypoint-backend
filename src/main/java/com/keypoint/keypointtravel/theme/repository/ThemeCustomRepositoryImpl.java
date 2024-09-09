package com.keypoint.keypointtravel.theme.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThemeCustomRepositoryImpl implements  ThemeCustomRepository{

    private final JPAQueryFactory queryFactory;

}
