package dotparser;

import Utils.HashUtils;
import Utils.StringUtils;
import changepropagation.GraphChangeListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class ClassNodeMaterial{
    private final String className;
    private Set<GraphChangeListener> listerners;
    private Marking marking = Marking.BLANK;

    public ClassNodeMaterial(final String className)
    {
        if((className != null) && (className.trim().equals("")))
        {
            throw new IllegalArgumentException("Name of Class not valid");
        }
        this.className = className;
        this.listerners = new HashSet<>();
    }

    public String getFullClassName()
    {
        return className.trim();
    }

    public String getSimpleClassName()
    {
            String sanitized = StringUtils.sanitizeStringForSimpleName(className);
            return sanitized.substring(sanitized.lastIndexOf(".") + 1).trim();
    }


    public Marking getMarking() {
        return marking;
    }

    public void setMarking(Marking marking)
    {
        this.marking = marking;
        notifyChangeListener();
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

            listener.update(this);
        }
    }
    public ClassNodeDAO getState()
    {
        return new ClassNodeDAO(null,null,null);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, className);
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
        final ClassNodeMaterial otherClassNodeFachwert = (ClassNodeMaterial) obj;
        return this.getSimpleClassName().equals(otherClassNodeFachwert.getSimpleClassName());
    }
}


