package de.hochschuletrier.gdw.commons.devcon.completers;

import java.util.List;

/**
 * Console command argument completion for booleans
 *
 * @author Santo Pfingsten
 */
public class BooleanCompleter implements IConsoleCompleter {

    private static final BooleanCompleter instance = new BooleanCompleter();

    public static IConsoleCompleter getInstance() {
        return instance;
    }

    private BooleanCompleter() {
    }

    @Override
    public void complete(String prefix, List<String> results) {
        final String lowerPrefix = prefix.toLowerCase();
        if ("true".startsWith(lowerPrefix)) {
            results.add("true");
        }
        if ("false".startsWith(lowerPrefix)) {
            results.add("false");
        }
    }
}
