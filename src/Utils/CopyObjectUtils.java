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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility-Klasse zum Klonen von Objekten mithilfe von Serialisierung
 *
 * @author Michael Inden
 *
 * Copyright 2011 by Michael Inden
 */
public final class CopyObjectUtils
{
    @SuppressWarnings("unchecked")
    public static <T extends Object & Serializable> T copyObject(final T original)
    {
        try
        {
            // Objekt in Byte-Array schreiben
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final ObjectOutputStream objectout = new ObjectOutputStream(bout);
            objectout.writeObject(original);

            // Objekt aus dem Byte-Array einlesen und konstrukieren
            final byte[] objBytes = bout.toByteArray();
            final ByteArrayInputStream bin = new ByteArrayInputStream(objBytes);
            final ObjectInputStream objectin = new ObjectInputStream(bin);

            return (T) objectin.readObject();
        }
        catch (final IOException e)
        {
            // unmöglich, da wir ein Byte-Array zur Ein- und Ausgabe nutzen
        }
        catch (final ClassNotFoundException e)
        {
            // unmöglich, da wir ein Objekt der Klasse gerade geschrieben haben
        }

        // unmöglicher Fall, Anweisung nur zur Vermeidung von Kompilierfehlern
        throw new IllegalStateException("copyObject() failed to copy object of type " + original.getClass());
    }

    private CopyObjectUtils()
    {
    }
}