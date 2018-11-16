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

package materials;

import Utils.HashUtils;
import service.GraphChangeListener;
import valueobjects.Marking;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nils-Pc on 06.08.2018.
 */

@XmlRootElement(name = "Class")
public class ClassNode {
    @XmlTransient
    private String _className;
    @XmlTransient
    private Set<GraphChangeListener> _listeners;
    @XmlTransient
    private Marking _marking = Marking.BLANK;
    @XmlTransient
    private Marking _oldMarking;

    private String _sourceFilePath = "";

    //JAXB
    public ClassNode()
    {}

    public ClassNode(final String className)
    {
        if((className != null) && (className.trim().equals("")))
        {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._listeners = ConcurrentHashMap.newKeySet();
        this._oldMarking = this._marking;
    }

    public ClassNode(final String className, Marking marking)
    {
        if((className != null) && (className.trim().equals("")))
        {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._listeners = new HashSet<>();
        this._marking = marking;
    }
    @XmlAttribute(name = "FQN")
    public String getFullClassName()
    {
        return _className;
    }

    @XmlAttribute(name = "Class")
    public String getSimpleClassName()
    {
        return new ClassNodeFormatter(this).toString();
    }

    @XmlAttribute(name = "Marking")
    public synchronized Marking getMarking() {
        return _marking;
    }

    /**
     * Sets a new marking if it differs form the old one.
     * Note: Changing the marking does not change the "ChangePropagationGraph".
     * @param marking
     */
    public void setMarking(final Marking marking)
    {
        if(this._marking != marking)
        {
            _oldMarking = _marking;
            this._marking = marking;
        }
    }
    @Nonnull
    public void setSourceFilePath(final String sourceFilePath)
    {
        if(sourceFilePath == null)
        {
            throw new IllegalArgumentException("Filepath of source should not be null");
        }
        if(!new File(sourceFilePath).isFile())
        {
            throw new IllegalArgumentException("The source filepath '" + sourceFilePath + "' is not a file!");
        }
        _sourceFilePath = sourceFilePath;
    }

    public String getSourceFilePath()
    {
        return _sourceFilePath;
    }

    @Override
    public String toString() {
        return new ClassNodeFormatter(this).toString();
    }

    public Marking getOldMarking() {
        return _oldMarking;
    }
    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, getSimpleClassName());
        return hash;
    }
    @Override
    public boolean equals(final Object obj)
    {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassNode otherClassNodeFachwert = (ClassNode) obj;
        return this.getSimpleClassName().equals(otherClassNodeFachwert.getSimpleClassName());
    }
}

