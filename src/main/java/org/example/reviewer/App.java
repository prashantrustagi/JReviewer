package org.example.reviewer;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class App {

    // Replace with your keys
    private static final String GOOGLE_KEY = "GEMINI_API_KEY";
    private static final String GROQ_KEY = "GROQ_API_KEY";

    public static void main(String[] args) {


        // 1. Initialize AI Service
        String standards;
        var resourceUrl = AgentFactory.class.getClassLoader().getResource("Standards.txt");
        try {
            assert resourceUrl != null;
            standards = Paths.get(resourceUrl.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        var reviewer = AgentFactory.createReviewer(GOOGLE_KEY, GROQ_KEY, standards);

        // 2. Initialize Engine
        var engine = new SlidingWindowEngine(reviewer);

        // 3. Run Analysis
        try {
            var codeUrl = App.class.getClassLoader().getResource("BadCode.java");
            if (codeUrl == null) throw new RuntimeException("BadCode.java not found in resources!");
            engine.analyzeFile(Paths.get(codeUrl.toURI()));
        } catch (Exception e) {
            System.err.println("Critical Error: " + e.getMessage());
        }
    }
}