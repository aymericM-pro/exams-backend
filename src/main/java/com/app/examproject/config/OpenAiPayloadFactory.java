package com.app.examproject.config;

import com.app.examproject.controller.generatorexam.GenerateExamRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for building the OpenAI payload used to generate exams.
 *
 * <p>
 * The expected JSON structure of the generated exam is documented by example
 * in the following resource file:
 * </p>
 *
 * <pre>
 * src/main/resources/openai/examples/generated-exam.json
 * </pre>
 *
 * @see <a href="classpath:/openai/examples/generated-exam.json">
 *      Example exam JSON structure
 *      </a>
 */
@Component
@RequiredArgsConstructor
public class OpenAiPayloadFactory {

    private final ObjectMapper mapper;

    public String examPayload(GenerateExamRequest request) throws Exception {
        ObjectNode root = mapper.createObjectNode();
        root.put("model", "gpt-4o-mini");

        ArrayNode input = mapper.createArrayNode();
        ObjectNode msg = mapper.createObjectNode();
        msg.put("role", "user");
        msg.put("content", buildPrompt(request));
        input.add(msg);
        root.set("input", input);

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

    /**
     * Builds the natural language prompt sent to the LLM.
     *
     * <p>
     * The prompt explicitly constrains the model to:
     * <ul>
     *   <li>Generate only multiple choice questions</li>
     *   <li>Return exactly four answers per question</li>
     *   <li>Mark exactly one answer as correct</li>
     *   <li>Return JSON only, without additional text</li>
     * </ul>
     *
     * These constraints reduce ambiguity and prevent invalid outputs.
     *
     * @param request exam generation parameters
     * @return formatted prompt string
     */
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

    /**
     * Builds the root JSON schema describing the expected exam structure.
     *
     * <p>
     * Example of a valid exam JSON produced by this schema:
     * </p>
     *
     * <pre>{@code
     * {
     *   "title": "Java Fundamentals Exam",
     *   "description": "Assessment covering core Java concepts including OOP and collections.",
     *   "semester": "S2",
     *   "questions": [ ... ]
     * }
     * }</pre>
     *
     * <p>
     * A complete example is available in:
     * </p>
     *
     * <pre>
     * src/main/resources/openai/examples/generated-exam.json
     * </pre>
     *
     * @return root JSON schema node for an exam
     */
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

    /**
     * Builds the JSON schema describing the questions array.
     *
     * <p>
     * Example of a question structure:
     * </p>
     *
     * <pre>{@code
     * {
     *   "title": "What is the main purpose of the JVM?",
     *   "type": "MULTIPLE_CHOICE",
     *   "position": 0,
     *   "answers": [ ... ]
     * }
     * }</pre>
     *
     * @return JSON schema node defining the questions list
     */
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

    /**
     * Builds the JSON schema describing the answers of a question.
     *
     * <p>
     * Example of a valid answers array (exactly one correct answer):
     * </p>
     *
     * <pre>{@code
     * [
     *   { "text": "To compile Java source code", "correct": false, "position": 0 },
     *   { "text": "To execute Java bytecode",    "correct": true,  "position": 1 },
     *   { "text": "To manage Maven dependencies","correct": false, "position": 2 },
     *   { "text": "To format Java code",          "correct": false, "position": 3 }
     * ]
     * }</pre>
     *
     * @return JSON schema node defining the answers array
     */
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

    /**
     * Builds a JSON schema node representing a string value.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>{@code
     * {
     *   "type": "string"
     * }
     * }</pre>
     *
     * @return JSON schema node for a string property
     */
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
