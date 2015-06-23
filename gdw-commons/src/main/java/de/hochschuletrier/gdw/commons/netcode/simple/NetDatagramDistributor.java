package de.hochschuletrier.gdw.commons.netcode.simple;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Distribute datagrams to their respective handler methods.
 * Caches all public methods named "handle", taking one parameter that extends NetDatagram.
 *
 * @author Santo Pfingsten
 */
public class NetDatagramDistributor {

    private static final Logger logger = LoggerFactory.getLogger(NetDatagramDistributor.class);

    private final HashMap<Class, Method> methods = new HashMap();
    private NetDatagramHandler handler;

    public void setHandler(NetDatagramHandler handler) {
        this.handler = handler;
        methods.clear();
        try {
            for (Method method : handler.getClass().getMethods()) {
                if (method.getName().equals("handle")) {
                    Class<?>[] types = method.getParameterTypes();
                    if (types.length == 1 && NetDatagram.class.isAssignableFrom(types[0])) {
                        methods.put(types[0], method);
                    }
                }
            }
        } catch (SecurityException e) {
            logger.error("Failed retrieving handle methods", e);
        }
    }

    public boolean handle(NetDatagram datagram) throws InvocationTargetException {
        if(handler == null) {
            logger.error("NetDatagramHandler has not been set!");
        } else {
            try {
                Method method = methods.get(datagram.getClass());
                if (method != null) {
                    method.invoke(handler, datagram);
                    return true;
                }
                logger.warn("Missing method void handle({}) in handler class {}!", datagram.getClass().getSimpleName(), handler.getClass().getSimpleName());
            } catch (IllegalAccessException e) {
                logger.error("Failed accessing method void handle({}) in handler class {}.. maybe it isn't public?", datagram.getClass().getSimpleName(), handler.getClass().getSimpleName(), e);
            } catch (IllegalArgumentException e) {
                logger.error("Failed calling method void handle({}) in handler class {}.. illegal argument?!?", datagram.getClass().getSimpleName(), handler.getClass().getSimpleName(), e);
            }
        }
        return false;
    }
}
