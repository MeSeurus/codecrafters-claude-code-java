import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.*;
import processing.ToolProcessor;
import tool.ActiveTools;
import tool.ReadTool;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2 || !"-p".equals(args[0])) {
            System.err.println("Usage: program -p <prompt>");
            System.exit(1);
        }

        String prompt = args[1];

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        ChatInteractor chatInteractor =
            new ChatInteractor(prompt, Collections.singletonList(new ReadTool().getTool()));

        List<ChatCompletionToolMessageParam> toolResults = new ArrayList<>();

        processInteractiveSession(chatInteractor, toolResults,0);

        System.out.println(chatInteractor.getMessage().content().orElse("could not find an answer"));
    }


    private static void processInteractiveSession(ChatInteractor chatInteractor,
                                                  List<ChatCompletionToolMessageParam> toolResults,
                                                  int step) {
        if (step != 0) {
            toolResults.clear();
        }
        ChatCompletionMessage message;
        ChatCompletion response = chatInteractor.getResponse();
        message = response.choices().getFirst().message();
        chatInteractor.addMessage(message.toParam());

        if (message.toolCalls().isPresent()) {
            toolResults.addAll(message.toolCalls().get().stream()
                .map(
                    tool -> {
                        Optional<ChatCompletionToolMessageParam> param = ToolProcessor.executeSingleToolCall(tool, new ReadTool().name());
                        return param.orElse(null);
                    })
                .filter(Objects::nonNull)
                .toList()
            );
            chatInteractor.addToolCallResults(toolResults);
        }
        if (!toolResults.isEmpty()) {
            processInteractiveSession(chatInteractor, toolResults, ++step);
        }
    }
}
