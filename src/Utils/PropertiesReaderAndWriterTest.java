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

import org.junit.Test;

import java.io.IOException;

public class PropertiesReaderAndWriterTest {

        @Test
        public void writeTest() {
            try {
                final PluginProperties appProperties = PluginProperties.getInstance();

                System.out.println("accessing property file '" + PluginProperties.getPropertyFilePath() + "'");
                appProperties.readPluginProperties();

                appProperties.setProperty(PropertyName.SWIFT_WORKSPACE, "hi2");
                appProperties.writeAppProperties();

                final String dbUser = appProperties.getProperty(PropertyName.ANDROID_WORKSPACE);
                final String dbPwd = appProperties.getProperty(PropertyName.SWIFT_WORKSPACE);
                appProperties.setProperty(PropertyName.ANDROID_WORKSPACE, "hi");

                System.out.println("DB USER / PWD= '" + dbUser + "' / '" + dbPwd + "'");
            }
            catch (IOException e)
            {
                System.out.println("Can't access property file '" + PluginProperties.getPropertyFilePath() + "'");
            }

        }
}
