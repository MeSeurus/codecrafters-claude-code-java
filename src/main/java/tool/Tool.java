package tool;

import com.openai.models.chat.completions.ChatCompletionFunctionTool;

/**
 * Common interface for all tools
 */
public interface Tool {

    /**
     * Name of the tool
     * @return name
     */
    String name();

    /**
     * Description of the tool
     * @return name
     */
    String description();

    /**
     * Returns the structure of the function defined with {@link ChatCompletionFunctionTool}
     * @return tool definition
     */
    ChatCompletionFunctionTool getTool();

    /**
     * Execute the instruction given with the tool
     * @return instruction result
     */
    String execute(String argument);
}
