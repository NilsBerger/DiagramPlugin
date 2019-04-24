package valueobjects;

/**
 * Defines the programming anguages a Class can have.
 */
public enum Language {
    Default("Default"),
    Java("Java"),
    Swift("Swift");

    private final String name;

    Language(String s) {
        name = s;
    }
}
