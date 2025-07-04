package com.jnjnetwork.CUHelper.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiServiceImpl implements GeminiService {
    private final RestTemplate rest = new RestTemplate();
    @Value("${google.palm.api.key}")
    private String apiKey;
    private static final String GEMINI_MODEL = "gemini-2.0-flash-lite-001";

    private static final String SYSTEM_PROMPT_TEMPLATE = """
                When the note for the session is provided, make feedback that is always based on the book that was discussed in the session.
                        
                The feedback style should be continuous and natural without listing responses in a numbered or segmented format. Avoid labeling responses with '1,' '2,' etc., and instead blend all observations into a single paragraph focusing on the student’s understanding and engagement with the story.
                        
                All feedback must always be in English. If constructive guidance is needed, it should be phrased in a way that highlights the student’s effort and progress.
                
                The feedback must be positive, but do not make things up or make it sound too cheesy.
                
                Avoid participial phrases introduced by commas. For example, do not use ", showing...", ", indicating", etc. Instead, write out a full clause or split into two sentences.
                                                                                                                                    
                For speaking feedback, explain the key points the student discussed and detail the guidance provided by the teacher, e.g., corrected grammar or vocabulary and how the corrections were applied.
                
                For writing feedback, outline what the student wrote about, the corrections or guidance provided by the teacher, and how these were incorporated.
                
                If the provided information appears to outline events from the story, assume those events were what was discussed.
                
                Use SIMPLE and CLEAR vocabulary. Do not use words that are overused by AI such as, "vivid," "grasp," etc.
                
                Student's name is not provided and never will. If there is a name in the provided information, then it is a character's name, not the student's. Always use <name> in the place of the student's name.
                
                Do NOT invent feedbacks or make things up to make the feedback sound better; Feedback MUST be based on what was provided.
                
                If the provided information is insufficient to generate feedback, return exactly "N/A"
                
                The teacher should be mentioned as "I" (as it is the teacher who is giving the feedback).
                
                Since the student’s name isn’t provided, do not assume their gender. If you need a third-person reference, use <name>.
                        
                Before providing a response, think ONCE MORE about these things before you provide a response: 1. Is the student being mentioned as <name>? 2. Did you check that the student's name should always be <name>? 3. Is the feedback length limited to 80 words? 4. Does the feedback follow the example provided, which is a one-paragraph continuous form without listing? 5. Are you using third-person and not using "you"? 6. Is the feedback is primarily based on the provided information? 7. Does the feedback contain any made up contents by you when it should not?
            """;

    @Override
    public String generateFeedback(String studentName, String note, String type) {
        String url = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                GEMINI_MODEL, apiKey
        );

        String fullPrompt = String.format("""
                %s
                ---
                Generate feedback based on the rules above for the following session:
                
                Feedback Type: %s
                Teacher's Notes:
                %s
                """, SYSTEM_PROMPT_TEMPLATE, type, note);

        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7,
                "candidateCount", 1
        );

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("role", "user",
                                "parts", List.of(
                                        Map.of("text", fullPrompt)
                                )
                        )
                ),
                "generationConfig", generationConfig
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        ResponseEntity<GeminiResponse> resp = rest.postForEntity(url, req, GeminiResponse.class);

        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API error: " + resp.getStatusCode() + " Body: " + resp.getBody());
        }

        // The rest of the code (DTO parsing) remains the same and is already robust.
        GeminiResponse geminiResponse = resp.getBody();
        if (geminiResponse != null && geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
            Candidate firstCandidate = geminiResponse.getCandidates().get(0);
            if (firstCandidate.getContent() != null && firstCandidate.getContent().getParts() != null && !firstCandidate.getContent().getParts().isEmpty()) {
                return firstCandidate.getContent().getParts().get(0).getText();
            }
        }

        return "No content received from the API.";
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class GeminiResponse {
    private List<Candidate> candidates;
    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Candidate {
    private Content content;
    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Content {
    private List<Part> parts;
    private String role;
    public List<Part> getParts() { return parts; }
    public void setParts(List<Part> parts) { this.parts = parts; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Part {
    private String text;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}