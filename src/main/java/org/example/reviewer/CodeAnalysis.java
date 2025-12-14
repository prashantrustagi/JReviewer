package org.example.reviewer;

import java.util.List;

public record CodeAnalysis(
        int cleanlinessScore,
        List<String> detectedBugs,
        String optimizedCode,
        String complexityAnalysis
) {}