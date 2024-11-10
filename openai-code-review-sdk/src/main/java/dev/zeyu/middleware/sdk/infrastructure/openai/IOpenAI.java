package dev.zeyu.middleware.sdk.infrastructure.openai;


import dev.zeyu.middleware.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import dev.zeyu.middleware.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

public interface IOpenAI {

    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception;

}
