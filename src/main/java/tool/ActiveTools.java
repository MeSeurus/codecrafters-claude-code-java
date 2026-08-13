package tool;

import lombok.Getter;

/**
 * List of tools and their status
 */
public enum ActiveTools {

    READ("Read", "Read and return the contents of a file", true);

    private final String name;
    private final String description;
    private final boolean isActive;

    ActiveTools(String name, String description, boolean isActive) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }
}
