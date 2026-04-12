package com.reconix.matching.dto;
public record MatchDetail(
    String strategyName, 
    double score, 
    String description
) {}
