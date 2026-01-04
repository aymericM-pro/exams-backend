package com.app.examproject.config;


import com.app.examproject.controller.generatorexam.dtos.ExamAiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Extracts and deserializes the structured JSON content returned
 * by the OpenAI Responses API into an {@link ExamAiResponse}.
 */
@Component
@RequiredArgsConstructor
public class OpenAiResponseExtractor {

    private final ObjectMapper mapper;

    /**
     * Parses the raw OpenAI HTTP response body and converts the extracted
     * {@code output_text} into an {@link ExamAiResponse}.
     *
     * @param body raw HTTP response body returned by OpenAI
     * @return deserialized {@link ExamAiResponse}
     * @throws Exception if the response does not contain a valid output_text
     *                   or cannot be deserialized
     */
    public ExamAiResponse extractExam(String body) throws Exception {
        JsonNode rootNode = mapper.readTree(body);
        String outputText = extractOutputText(rootNode);

        if (outputText == null || outputText.isBlank()) {
            throw new IllegalStateException("No output_text in OpenAI response");
        }

        JsonNode json = mapper.readTree(outputText);

        if (!json.isObject()) {
            throw new IllegalStateException("Invalid OpenAI response: root is not an object");
        }

        return mapper.treeToValue(json, ExamAiResponse.class);
    }

    /**
     * Retrieves the {@code output_text} field from the OpenAI response structure.
     *
     * @param rootNode root JSON node of the OpenAI response
     * @return extracted JSON text or {@code null} if not found
     */
    private String extractOutputText(JsonNode rootNode) {
        JsonNode outputs = rootNode.path("output");
        if (!outputs.isArray()) {
            throw new IllegalStateException("Invalid OpenAI response: output is not an array");
        }

        for (JsonNode out : outputs) {
            JsonNode content = out.path("content");
            if (!content.isArray()) {
                throw new IllegalStateException("Invalid OpenAI response: content is not an array");
            }

            for (JsonNode c : content) {
                if ("output_text".equals(c.path("type").asText())) {
                    return c.path("text").asText(null);
                }
            }
        }
        return null;
    }
}
