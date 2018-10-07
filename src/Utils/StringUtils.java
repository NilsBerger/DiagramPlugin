
/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Utils;
public abstract class StringUtils {
    public static String sanitizeStringForSimpleName(final String className)
    {
        String name = StringUtils.deleteQuotationMarks(className);
        name = StringUtils.deleteAllTextInBrackets(name);
        name = StringUtils.deleteJarEnding(name);
        name = StringUtils.deleteSemicolon(name);
        return name.trim();
    }
    private static String deleteQuotationMarks(final String string)
    {
        return string.replace("\"","");
    }
    private static String deleteAllTextInBrackets(final String string)
    {
        return string.replaceAll("\\(.*\\)", "");
    }
    private static String deleteJarEnding(final String string)
    {
        return string.replace(".jar","");
    }
    private static String deleteSemicolon(final String string)
    {
        return string.replace(";","");
    }
}
