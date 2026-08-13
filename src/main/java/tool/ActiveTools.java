package tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * List of tools and their status
 */
@AllArgsConstructor
@Getter
public enum ActiveTools {

    READ("Read", "Read and return the contents of a file", true);

    private final String name;
    private final String description;
    private final boolean isActive;

}
