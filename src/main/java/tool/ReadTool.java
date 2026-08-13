package tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.core.JsonValue;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.chat.completions.ChatCompletionFunctionTool;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation for 'READ' tool
 */
public class ReadTool implements Tool {

    private static final Map<String, String> subPropertiesMap = Map.of(
        "type", "string",
        "description", "The path to the file to read"
    );

    private static final Map<String, Map<String, String>> propertiesMap = Map.of(
        "file_path", subPropertiesMap
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "Read";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description() {
        return "Read and return the contents of a file";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatCompletionFunctionTool getTool() {
        return ChatCompletionFunctionTool.builder()
            .function(
                FunctionDefinition.builder()
                    .name(name())
                    .description(description())
                    .parameters(
                        FunctionParameters.builder()
                            .putAdditionalProperty(
                                "type", JsonValue.from("object"))
                            .putAdditionalProperty(
                                "properties", JsonValue.from(propertiesMap))
                            .putAdditionalProperty(
                                "required", JsonValue.from(List.of("file_path")))
                            .build())
                    .build())
            .build();
    }

    @Override
    public String execute(String argument) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ReadArgs args = objectMapper.readValue(argument, ReadArgs.class);
            return Files.readString(Paths.get(args.file_path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static class ReadArgs {
        public String file_path;
    }
}
