package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.openapi.graph.util.DataProviderAdapter;

public class ClassDataProviderAdapater implements DataProviderAdapter {
    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public int getInt(Object o) {
        return 0;
    }

    @Override
    public double getDouble(Object o) {
        return 0;
    }

    @Override
    public boolean getBool(Object o) {
        return false;
    }

    @Override
    public boolean defined(Object o) {
        return false;
    }
}
