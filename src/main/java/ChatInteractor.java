import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.*;

import java.util.List;

public class ChatInteractor {

    private OpenAIClient client;

    public ChatInteractor(String prompt, List<ChatCompletionFunctionTool> toolsList) {
        String apiKey = System.getenv("OPENROUTER_API_KEY");
        String baseUrl = System.getenv("OPENROUTER_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://openrouter.ai/api/v1";
        }

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OPENROUTER_API_KEY is not set");
        }

        OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(apiKey)
            .baseUrl(baseUrl)
            .build();

        ChatCompletion response = client.chat().completions().create(
            ChatCompletionCreateParams.builder()
                .model("anthropic/claude-haiku-4.5")
                .addUserMessage(prompt)
                .addTool(toolsList.getFirst())
                .build()
        );
    }

    public void addMessage(ChatCompletionAssistantMessageParam param) {
        ChatCompletionCreateParams.builder().addMessage(param);
    }

    public void addToolCallResults(List<ChatCompletionToolMessageParam> toolMessageParams) {
        toolMessageParams.forEach(
            param -> ChatCompletionCreateParams.builder().addMessage(param)
        );
    }

    public ChatCompletion getResponse() {
        return client.chat().completions().create(
            ChatCompletionCreateParams.builder().build()
        );
    }

    public ChatCompletionMessage getMessage() {
        return message;
    }

}
