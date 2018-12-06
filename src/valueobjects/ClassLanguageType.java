package valueobjects;

/**
 * Defines the programming anguages a Class can have.
 */
public enum ClassLanguageType {
    Default("Default"),
    Java("Java"),
    Swift("Swift");

    private final String name;

    ClassLanguageType(String s) {
        name = s;
    }
}
