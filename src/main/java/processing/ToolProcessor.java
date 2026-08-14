package processing;

import com.openai.core.JsonValue;
import com.openai.models.chat.completions.ChatCompletionMessageFunctionToolCall;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import com.openai.models.chat.completions.ChatCompletionToolMessageParam;
import tool.ReadTool;
import tool.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ToolProcessor {

    private static final List<Tool> toolsList = Collections.singletonList(new ReadTool());

    public static Optional<ChatCompletionToolMessageParam> executeSingleToolCall(ChatCompletionMessageToolCall tool, String toolNameToProcess) {
        Optional<ChatCompletionToolMessageParam> result = Optional.empty();
        if (tool.function().isPresent()) {
            ChatCompletionMessageFunctionToolCall functionMap = tool.function().get();
            if (functionMap.function().name().equals(toolsList.getFirst().name())) {
                if (toolsList.getFirst().name().equals(toolNameToProcess)) {
                    result = Optional.of(ChatCompletionToolMessageParam.builder()
                        .toolCallId(tool.function().get().id())
                        .content(toolsList.getFirst().execute(tool.function().get().function().arguments()))
                        .build());
                }
            }
        }
        return result;
    }
}
