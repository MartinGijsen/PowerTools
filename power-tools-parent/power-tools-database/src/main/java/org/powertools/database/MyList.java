package org.powertools.database;

import java.util.LinkedList;
import java.util.List;
import org.powertools.engine.ExecutionException;


final class MyList {
    private final String mName;
    private final List<ListItem> mItems;
    
    MyList (String name) {
        mName  = name;
        mItems = new LinkedList<ListItem> ();
    }
    
    void add (ListItem item) {
        mItems.add (item);
    }
    
    @Override
    public String toString () {
        if (mItems.isEmpty ()) {
            throw new ExecutionException ("query contains no " + mName);
        }

        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (ListItem item : mItems) {
            if (isFirst) {
                sb.append (item.toString ());
                isFirst = false;
            } else {
                sb.append (", ").append (item.toString());
            }
        }
        return sb.toString ();
    }
}
