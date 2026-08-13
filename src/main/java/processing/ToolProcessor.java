package processing;

import com.openai.core.JsonValue;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import tool.ReadTool;
import tool.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ToolProcessor {

    private static final List<Tool> toolsList = Collections.singletonList(new ReadTool());

    public static Map<String, String> executeSingleToolCall(ChatCompletionMessageToolCall tool, String toolNameToProcess) {
        Map<String, JsonValue> functionMap;
        Map<String, String> result = Map.of();
        if (tool.function().isPresent()) {
            functionMap = tool.function().get()._function().asObject().orElse(null);
            if (null != functionMap && functionMap.containsKey(toolNameToProcess)) {
                if (toolsList.getFirst().name().equals(toolNameToProcess)) {
                    result = Map.of(
                        tool.function().get().id(),
                        toolsList.getFirst().execute(tool.function().get().function().arguments())
                    );
                }
            }
        }
        System.out.println(result.toString());
        return result;
    }
}
