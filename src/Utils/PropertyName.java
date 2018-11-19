package Utils;

public enum PropertyName
{
    ANDROID_WORKSPACE("android.workspace"), SWIFT_WORKSPACE("swift.workspace"), Swift_SCHEME_FOLDER("swift.scheme");

    final String propertyKey;

    PropertyName(final String propertyKey)
    {
        this.propertyKey = propertyKey;
    }
}