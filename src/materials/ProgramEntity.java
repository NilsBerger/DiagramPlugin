package materials;

import Utils.HashUtils;
import valueobjects.ClassNodeType;
import valueobjects.Language;
import valueobjects.Marking;

import java.io.File;

/**
 * A ClassNode represents a class entity, that was identified at the dependency analysis.
 */
public class ProgramEntity {
    private String _className;
    private Marking _marking = Marking.BLANK;
    private boolean _hide = false;
    private final Language _Language;
    private ClassNodeType _classNodeType = ClassNodeType.CLASS;
    private String _sourceFilePath = "";
    private boolean _initialClass;
    private ProgramEntity _actuator;


    public ProgramEntity(final String className, Language type) {
        if ((className != null) && (className.trim().equals(""))) {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._Language = type;
    }

    public ProgramEntity(final String className, Marking marking, Language type) {
        if ((className != null) && (className.trim().equals(""))) {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this._className = className;
        this._marking = marking;
        this._Language = type;
    }

    /**
     * Sets the filepath to the Java-Class that the ClassNode is representing
     *
     * @param sourceFilePath path to the file representing the ClassNode
     */
    void setSourceFilePath(final String sourceFilePath) {
        if (sourceFilePath == null) {
            throw new IllegalArgumentException("Filepath of source should not be null");
        }
        if (!new File(sourceFilePath).isFile()) {
            throw new IllegalArgumentException("The source filepath '" + sourceFilePath + "' is not a file!");
        }
        _sourceFilePath = sourceFilePath;
    }

    /**
     * Sets a new marking if it differs form the old one.
     */
    public void setMarking(final Marking marking) {
        this._marking = marking;
    }

    /**
     * Sets the ClassNodeType of the ClassNode.
     */
    public void setClassNodeType(final ClassNodeType classNodeType) {
        _classNodeType = classNodeType;
    }

    /**
     * Defines if a ClassNode should be hidden in the diagram.
     *
     * @param hide set true to hide.
     */
    public void setHide(boolean hide) {
        _hide = hide;
    }

    /**
     * To set if a ClassNode was found through the Concept Location
     */
    public void setAsInitialClass() {
        _initialClass = true;
    }

    /**
     * Stors the ClassNode, that triggered he neighbourhood. The Actuator can only be set once.
     *
     * @param actuator The ClassNode that triggered the "neighbourhood"
     */
    public void setActuator(final ProgramEntity actuator) {
        if (_actuator == null && !_initialClass) {
            _actuator = actuator;
        }
        if (isInitialClass()) {
            _actuator = null;
        }
    }

    /**
     * Returns the Name of the ClassNode, that
     * Note: The name can
     */
    public String getFullEntityName() {
        return _className;
    }


    /**
     * Returns the simple name of a Java-Class. Not the FQN.
     */
    public String getSimpleName() {
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
    public ClassNodeType getClassNodeType() {
        return _classNodeType;
    }

    /**
     * Returns the getLanguage of the ClassNode. The getLanguage is final.
     */
    public Language getLanguage() {
        return _Language;
    }

    /**
     * Return if a ClassNode was found through the concept location
     *
     * @return True if initial
     */
    public boolean isInitialClass() {
        return _initialClass;
    }

    public String getActuator() {
        if (_actuator != null) {
            return _actuator.getSimpleName();
        }
        return "Initial";
    }

    /**
     * Returns if the node is currently hidden in the diagram.
     */
    public boolean isHidden() {
        return _hide;
    }

    /**
     * Returns the filepath to the Java-Class that the ClassNode represents
     * Note: The String can be empty
     */
    String getSourceFilePath() {
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
        hash = HashUtils.calcHashCode(hash, _Language);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        final ProgramEntity otherProgramEntityFachwert = (ProgramEntity) obj;
        return this.getSimpleName().equals(otherProgramEntityFachwert.getSimpleName()) &&
                this._Language == otherProgramEntityFachwert.getLanguage();
    }
}


