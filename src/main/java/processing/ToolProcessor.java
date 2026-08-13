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
        if (tool.function().isPresent()) {
            functionMap = tool.function().get()._function().asObject().orElse(null);
            if (null != functionMap && functionMap.containsKey(toolNameToProcess)) {
                for (Tool knownTool : toolsList) {
                    if (knownTool.name().equals(toolNameToProcess)) {
                        return Map.of(
                            tool.function().get().id(),
                            knownTool.execute(tool.function().get().function().arguments())
                        );
                    }
                }
            }
        }
        return Map.of();
    }
}
