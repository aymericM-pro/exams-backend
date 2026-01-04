package com.app.examproject.config;

import com.app.examproject.controller.generatorexam.GenerateExamRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiPayloadFactory {

    private final ObjectMapper mapper;

    public String examPayload(GenerateExamRequest request) throws Exception {
        ObjectNode root = mapper.createObjectNode();
        root.put("model", "gpt-4o-mini");

        // ===== input =====
        ArrayNode input = mapper.createArrayNode();
        ObjectNode msg = mapper.createObjectNode();
        msg.put("role", "user");
        msg.put("content", buildPrompt(request));
        input.add(msg);
        root.set("input", input);

        // ===== structured output (CONTRAT) =====
        ObjectNode text = mapper.createObjectNode();
        ObjectNode format = mapper.createObjectNode();
        format.put("type", "json_schema");
        format.put("name", "exam");
        format.put("strict", true);
        format.set("schema", buildExamSchema());
        text.set("format", format);
        root.set("text", text);

        return mapper.writeValueAsString(root);
    }

    private String buildPrompt(GenerateExamRequest request) {
        return """
Generate an exam.

Theme: %s
Number of questions: %d
Difficulty level: %s

All questions MUST be multiple choice.
Each question MUST have exactly 4 answers.
Exactly one answer MUST have correct=true.

Return JSON only.
""".formatted(
                request.theme(),
                request.questionCount(),
                request.level() != null ? request.level() : "intermediate"
        );
    }

    // ===== JSON SCHEMA =====

    private ObjectNode buildExamSchema() {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");
        schema.put("additionalProperties", false);

        schema.putArray("required")
                .add("title")
                .add("description")
                .add("semester")
                .add("questions");

        ObjectNode props = mapper.createObjectNode();
        props.set("title", string());
        props.set("description", string());
        props.set("semester", string());
        props.set("questions", questionsSchema());

        schema.set("properties", props);
        return schema;
    }

    private ObjectNode questionsSchema() {
        ObjectNode q = mapper.createObjectNode();
        q.put("type", "array");

        ObjectNode item = mapper.createObjectNode();
        item.put("type", "object");
        item.put("additionalProperties", false);

        item.putArray("required")
                .add("title")
                .add("type")
                .add("position")
                .add("answers");

        ObjectNode props = mapper.createObjectNode();
        props.set("title", string());
        props.set("type", string());
        props.set("position", integerMin0());
        props.set("answers", answersSchema());

        item.set("properties", props);
        q.set("items", item);
        return q;
    }

    private ObjectNode answersSchema() {
        ObjectNode a = mapper.createObjectNode();
        a.put("type", "array");
        a.put("minItems", 4);

        ObjectNode item = mapper.createObjectNode();
        item.put("type", "object");
        item.put("additionalProperties", false);

        item.putArray("required")
                .add("text")
                .add("correct")
                .add("position");

        ObjectNode props = mapper.createObjectNode();
        props.set("text", string());
        props.set("correct", bool());
        props.set("position", integerMin0());

        item.set("properties", props);
        a.set("items", item);
        return a;
    }


    private ObjectNode string() {
        return mapper.createObjectNode().put("type", "string");
    }

    private ObjectNode integerMin0() {
        return mapper.createObjectNode()
                .put("type", "integer")
                .put("minimum", 0);
    }

    private ObjectNode bool() {
        return mapper.createObjectNode().put("type", "boolean");
    }
}
