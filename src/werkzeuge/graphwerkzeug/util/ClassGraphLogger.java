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

package werkzeuge.graphwerkzeug.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassGraphLogger {

    private static com.intellij.openapi.diagnostic.Logger logger;

    static {
        logger = com.intellij.openapi.diagnostic.Logger.getInstance("graphapi");
    }

    public static void debug(String message) {
        logger.debug(getPrefix() + message);
    }

    private static String getPrefix() {
        return "[" + getTime() + "] ";
    }

    private static String getTime() {
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date now = new Date();
        return format.format(now);
    }
}
