package com.app.examproject.services.impl;

import com.app.examproject.config.OpenAiPayloadFactory;
import com.app.examproject.config.OpenAiResponseExtractor;
import com.app.examproject.controller.generatorexam.GenerateExamRequest;
import com.app.examproject.controller.generatorexam.dtos.ExamAiResponse;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private static final String URL = "https://api.openai.com/v1/responses";

    private final OkHttpClient client;
    private final OpenAiPayloadFactory payloadFactory;
    private final OpenAiResponseExtractor responseExtractor;

    @Value("${openai.api.key}")
    private String apiKey;

    public ExamAiResponse generateExam(GenerateExamRequest request) {
        try {
            String payload = payloadFactory.examPayload(request);

            Request httpRequest = new Request.Builder()
                    .url(URL)
                    .post(RequestBody.create(payload, MediaType.parse("application/json")))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                String body = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    throw new IllegalStateException(
                            "OpenAI HTTP " + response.code() + " : " + body
                    );
                }

                return responseExtractor.extractExam(body);
            }

        } catch (java.net.SocketTimeoutException e) {
            // seul cas externe attendu
            throw new RuntimeException("OpenAI timeout", e);

        } catch (Exception e) {
            // tout le reste = bug ou contrat cassé
            throw new RuntimeException("OpenAI call failed", e);
        }
    }

}
