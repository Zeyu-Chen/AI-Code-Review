package dev.zeyu.middleware.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.zeyu.middleware.sdk.domain.service.impl.OpenAICodeReviewService;
import dev.zeyu.middleware.sdk.infrastructure.git.GitCommand;
import dev.zeyu.middleware.sdk.infrastructure.openai.IOpenAI;
import dev.zeyu.middleware.sdk.infrastructure.openai.impl.ChatGPT;
import dev.zeyu.middleware.sdk.infrastructure.wechat.WeChat;

public class OpenAICodeReview {

    private static final Logger logger = LoggerFactory.getLogger(OpenAICodeReview.class);

    public static void main(String[] args) throws Exception {
        GitCommand gitCommand = new GitCommand(
                getEnv("GITHUB_REVIEW_LOG_URI"),
                getEnv("GITHUB_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        WeChat weChat = new WeChat(
                getEnv("WECHAT_APPID"),
                getEnv("WECHAT_SECRET"),
                getEnv("WECHAT_TOUSER"),
                getEnv("WECHAT_TEMPLATE_ID")
        );

        IOpenAI openAI = new ChatGPT(getEnv("OPENAI_APIHOST"), getEnv("OPENAI_APIKEYSECRET"));

        OpenAICodeReviewService openAiCodeReviewService = new OpenAICodeReviewService(gitCommand, openAI, weChat);
        openAiCodeReviewService.exec();

        logger.info("openai-code-review done!");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("value is null");
        }
        return value;
    }
}
