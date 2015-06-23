package de.hochschuletrier.gdw.commons.devcon.completers;

import java.util.List;

/**
 * Console command argument completion for enumerations
 *
 * @author Santo Pfingsten
 */
public class EnumCompleter<T extends Enum> implements IConsoleCompleter {

    Class<T> clazz;

    public EnumCompleter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void complete(String prefix, List<String> results) {
        final String lowerPrefix = prefix.toLowerCase();
        for (T o : clazz.getEnumConstants()) {
            if (o.name().toLowerCase().startsWith(lowerPrefix)) {
                results.add(o.name());
            }
        }
    }
}
