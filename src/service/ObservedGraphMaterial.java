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

package service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class ObservedGraphMaterial {
    private Set<GraphChangeListener> listerners;

    public ObservedGraphMaterial()
    {
        listerners = new HashSet<>();
    }

    public void addGraphChangeListener(final GraphChangeListener observer)
    {
        assert observer != null : "Precondition violated: !=null";
        listerners.add(observer);
    }

    public void removeChangeListener(final GraphChangeListener observer)
    {
        assert observer != null : "Precondition violated: !=null";
        listerners.add(observer);
    }
    protected synchronized void notifyChangeListener()
    {
        final Iterator<GraphChangeListener> it = listerners.iterator();
        while (it.hasNext())
        {
            final GraphChangeListener listener = it.next();

            listener.update(null);
        }
    }

}
