package de.hochschuletrier.gdw.commons.resourcelocator;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author Santo Pfingsten
 */
public interface IResourceLocator {

    InputStream locateResource(String filename) throws FileNotFoundException;

    String combinePaths(String base, String filename);
}
