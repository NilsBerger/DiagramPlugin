package materials;

import Utils.HashUtils;
import valueobjects.ClassLanguageType;
import valueobjects.ClassNodeType;
import valueobjects.Marking;

import java.io.File;

/**
 * A ClassNode represents a class entity, that was identified at the dependency analysis.
 */
public class ClassNode {
    private String _className;
    private Marking _marking = Marking.BLANK;
    private boolean _hide = false;
    private final ClassLanguageType _classLanguageType;
    private ClassNodeType _classNodeType = ClassNodeType.CLASS;
    private String _sourceFilePath = "";



    public ClassNode(final String className, ClassLanguageType type)
    {
        if((className != null) && (className.trim().equals("")))
        {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._classLanguageType = type;
    }

    public ClassNode(final String className, Marking marking, ClassLanguageType type)
    {
        if((className != null) && (className.trim().equals("")))
        {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._marking = marking;
        this._classLanguageType = type;
    }

    /**
     * Sets the filepath to the Java-Class that the ClassNode is representing
     * @param sourceFilePath path to the file representing the ClassNode
     */
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

    /**
     * Sets a new marking if it differs form the old one.
     */
    public void setMarking(final Marking marking)
    {
        this._marking = marking;
    }

    /**
     * Sets the ClassNodeType of the ClassNode.
     */
    public void setClassNodeType(final ClassNodeType classNodeType)
    {
        _classNodeType = classNodeType;
    }

    /**
     * Defines if a ClassNode should be hidden in the diagram.
     * @param hide set true to hide.
     */
    public void setHide(boolean hide)
    {
        _hide = hide;
    }

    /**
     * Returns the Name of the ClassNode, that
     * Note: The name can
     */
    public String getFullClassName()
    {
        return _className;
    }

    /**
     * Returns the simple name of a Java-Class. Not the FQN.
     */
    public String getSimpleName()
    {
        return new ClassNodeFormatter(this).toString();
    }

    /**
     * Return the current Marking of the ClassNode
     */
    public Marking getMarking() {
        return _marking;
    }
    /*+
     * Returns the ClassNodeTyp. The default value is "Class".
     */
    public ClassNodeType getClassNodeType()
    {
        return _classNodeType;
    }

    /**
     * Returns the getClassLanguageType of the ClassNode. The getClassLanguageType is final.
     */
    public ClassLanguageType getClassLanguageType()
    {
        return _classLanguageType;
    }

    /**
     * Returns if the node is currently hidden in the diagram.
     */
    public boolean isHidden()
    {
        return _hide;
    }

    /**
     * Returns the filepath to the Java-Class that the ClassNode represents
     * Note: The String can be empty
     */
    public String getSourceFilePath()
    {
        return _sourceFilePath;
    }

    @Override
    public String toString() {
        return new ClassNodeFormatter(this).toString();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, getSimpleName());
        hash = HashUtils.calcHashCode(hash, _classLanguageType);
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
        return this.getSimpleName().equals(otherClassNodeFachwert.getSimpleName()) &&
                this._classLanguageType == otherClassNodeFachwert.getClassLanguageType();
    }
}


