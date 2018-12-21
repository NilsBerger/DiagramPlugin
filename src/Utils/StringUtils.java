package Utils;
public abstract class StringUtils {
    public static String sanitizeStringForSimpleName(final String className) {
        String name = StringUtils.deleteQuotationMarks(className);
        name = StringUtils.deleteAllTextInBrackets(name);
        name = StringUtils.deleteJarEnding(name);
        name = StringUtils.deleteJavaEnding(name);
        name = StringUtils.deleteSemicolon(name);
        name = StringUtils.trimFQN(name);
        name = StringUtils.deleteAllAfterDollarSign(name);
        return name.trim();
    }

    private static String deleteQuotationMarks(final String string) {
        return string.replace("\"", "");
    }

    private static String deleteAllTextInBrackets(final String string) {
        return string.replaceAll("\\(.*\\)", "");
    }

    private static String deleteJarEnding(final String string) {
        return string.replace(".jar", "");
    }

    private static String deleteJavaEnding(final String string){return string.replace(".java", "");}

    private static String deleteSemicolon(final String string) {
        return string.replace(";", "");
    }

    private static String deleteAllAfterDollarSign(final String string) {
        String[] stringarray = string.split("\\$");
        for(int counter = stringarray.length - 1; counter >= 0; counter--)
        {
            String tmpString = stringarray[counter];
            if(!containsDigit(tmpString) && tmpString.length() >= 1)
            {
                return tmpString;
            }
        }
        return stringarray[0];
    }

    private static String trimFQN(final String string)
    {
        return  string.substring(string.lastIndexOf(".") + 1).trim();

    }

    public static final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }
        return containsDigit;
    }

}
