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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Beispiel für das Auslesen von Properties aus einer Konfigurationsdatei
 * <br>
 * Verbesserung der Unzulänglichkeiten der Klasse Properties durch
 * Definition einer eigenen Klassen PluginProperties, die sich auf die
 * Klasse Properties stützt
 *
 * @author Michael Inden
 *
 * Copyright 2011 by Michael Inden
 */
public final class PluginProperties
{
    private static final String  FILE_PATH  = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/resources/AppConfig.properties";

    private static PluginProperties instance;

    private final Properties     properties = new Properties();

    public static synchronized PluginProperties getInstance() throws IOException
    {
        if (instance == null)
        {
            // Implementierungstrick: Nur nach erfolgreichem Laden wird
            // "instance" gesetzt, ansonsten wird eine Exception ausgelöst
            instance = new PluginProperties();
        }
        return instance;
    }

    private PluginProperties() throws IOException
    {
        readPluginProperties();
    }

//    private File getFilePath()
//    {
//        URL url = getClass().getResource("resources/AppConfig.properties");
//        return new File(url.getFile());
//    }

    public synchronized void readPluginProperties() throws IOException
    {
        InputStream in = null;
        try
        {
            in = new FileInputStream(FILE_PATH);
            properties.load(in);
        }
        finally
        {
            StreamUtils.safeClose(in);
        }
    }

    // ...
    public synchronized void writeAppProperties() throws IOException
    {
        OutputStream out = null;
        try
        {
            out = new FileOutputStream(FILE_PATH);
            properties.store(out, "");
        }
        finally
        {
            StreamUtils.safeClose(out);
        }
    }

    public synchronized String getProperty(final PropertyName key)
    {
        return properties.getProperty(key.propertyKey);
    }

    public synchronized void setProperty(final PropertyName key, final String value)
    {
        properties.setProperty(key.propertyKey, value);
    }

    public static String getPropertyFilePath()
    {
        return new File(FILE_PATH).getAbsolutePath();
    }

}

