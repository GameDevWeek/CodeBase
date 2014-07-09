package de.hochschuletrier.gdw.commons.utils.id;

import java.util.HashSet;
import java.util.Stack;

/**
 * 
 * @author ElFapo
 */
public class Identifier {

    private final Stack<Long> freeIDs = new Stack();
    private final HashSet<Long> takenIDs = new HashSet();
    private final HashSet<Long> allIDs = new HashSet();

    private long idCount;

    public Identifier(long initialIDCount) {
        idCount = initialIDCount;

        for (long i = 1; i <= idCount; i++) {
            freeIDs.push(i);
            allIDs.add(i);
        }
    }

    public Long requestID() {
        if (freeIDs.isEmpty()) {
            long id = ++idCount;
            freeIDs.push(id);
            allIDs.add(id);
        }

        Long id = freeIDs.pop();
        takenIDs.add(id);
        return id;
    }

    public void returnID(Long id) {
        if (doesExist(id) && isTaken(id)) {
            takenIDs.remove(id);
            freeIDs.push(id);
        }
    }

    public boolean doesExist(Long id) {
        return allIDs.contains(id);
    }

    public boolean isTaken(Long id) {
        return takenIDs.contains(id);
    }
}
