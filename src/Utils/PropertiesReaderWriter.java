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
import java.io.*;
import java.util.*;


public class PropertiesReaderWriter {
    public static final String ANDROID_WORKSPACE_KEY = "Android_Workspace";
    public static final String SWIFT_WORK_SPACE_KEY = "Swift_Workspace";
    private static final String propertiesPath = "resources/paths.properties";

    public static void writeWorkspacePath(final String key, final String path) {
            Properties prop = new Properties();
            OutputStream output = null;
            InputStream input = null;

            try {

                output = new FileOutputStream(propertiesPath);
                input = new FileInputStream(propertiesPath);
                prop.load(input);

                HashMap<String, String> propertyMap = new HashMap();
                for(String propkey: prop.stringPropertyNames())
                {
                    propertyMap.put(propkey,prop.getProperty(propkey));
                    System.out.println(propkey);
                }
                propertyMap.put(key, path);

                //Write map to property
                for(Map.Entry<String, String> pair : propertyMap.entrySet())
                {
                    prop.setProperty(pair.getKey(), pair.getValue());
                }

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    public static String readWorkspacePath(final String key) {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(propertiesPath);
            prop.load(input);
            return prop.getProperty(key);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
