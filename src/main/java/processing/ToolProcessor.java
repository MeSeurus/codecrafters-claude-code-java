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
        System.out.println("Tool is present? " + String.valueOf(tool.function().isPresent()));
        if (tool.function().isPresent()) {
            System.out.println(tool.function().get().function());
            functionMap = tool.function().get()._function().asObject().get();
            System.out.println(functionMap.get("name").asStringOrThrow());
            if (null != functionMap && functionMap.get("name").asStringOrThrow().equals(toolsList.getFirst().name())) {
                System.out.println("FunctionMap is: " + functionMap.toString());
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
