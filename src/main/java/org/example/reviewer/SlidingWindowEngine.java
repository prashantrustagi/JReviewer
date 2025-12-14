package org.example.reviewer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SlidingWindowEngine {

    private final AgentFactory.CodeReviewer reviewer;

    public SlidingWindowEngine(AgentFactory.CodeReviewer reviewer) {
        this.reviewer = reviewer;
    }

    public void analyzeFile(Path path) throws IOException {
        System.out.println("🚀 [Engine] Starting analysis for: " + path.getFileName());

        List<String> allLines = Files.readAllLines(path);
        int totalLines = allLines.size();
        int windowSize = 20;
        int overlap = 5;

        List<String> aggregatedBugs = new ArrayList<>();
        int totalScore = 0;
        int chunksProcessed = 0;

        for (int i = 0; i < totalLines; i += (windowSize - overlap)) {
            int end = Math.min(totalLines, i + windowSize);

            // Extract Chunk
            String chunk = String.join("\n", allLines.subList(i, end));

            System.out.print("   Processing lines " + i + "-" + end + "... ");

            try {
                // Call AI
                CodeAnalysis result = reviewer.analyze(chunk);

                System.out.println("✅ Score: " + result.cleanlinessScore());
                if (result.detectedBugs() != null) {
                    aggregatedBugs.addAll(result.detectedBugs());
                }
                totalScore += result.cleanlinessScore();
                chunksProcessed++;

                if (end == totalLines) break;
            } catch (Exception e) {
                System.out.println("❌ Failed: " + e.getMessage());
            }
        }

        printReport(path.getFileName().toString(), chunksProcessed, totalScore, aggregatedBugs);
    }

    private void printReport(String fileName, int chunks, int totalScore, List<String> bugs) {
        System.out.println("\n==========================================");
        System.out.println("       FINAL REPORT: " + fileName);
        System.out.println("==========================================");
        System.out.println("Average Quality Score: " + (chunks > 0 ? totalScore / chunks : 0) + "/10");
        System.out.println("Total Policy Violations: " + bugs.size());
        System.out.println("------------------------------------------");
        bugs.forEach(bug -> System.out.println("🔴 " + bug));
        System.out.println("==========================================");
    }
}