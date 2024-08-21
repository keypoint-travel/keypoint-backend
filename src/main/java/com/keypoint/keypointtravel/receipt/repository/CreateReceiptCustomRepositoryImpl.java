package com.keypoint.keypointtravel.receipt.repository;

import org.springframework.data.domain.AuditorAware;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateReceiptCustomRepositoryImpl implements CreateReceiptCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

}
