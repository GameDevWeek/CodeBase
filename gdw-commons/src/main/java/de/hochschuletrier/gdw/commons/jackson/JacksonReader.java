package de.hochschuletrier.gdw.commons.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reading Json to objects
 *
 * @author Santo Pfingsten
 */
public class JacksonReader {

    private static final JsonFactory factory = new JsonFactory();

    public static <RT> RT read(String filename, Class<RT> clazz)
            throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, ParseException {

        try (InputStream fileIn = CurrentResourceLocator.read(filename);
                InputStreamReader inReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inReader);
                JsonParser parser = factory.createParser(bufferedReader);) {
            parser.nextToken();
            return readUnknownObject(clazz, parser);
        }
    }

    public static <RT> HashMap<String, RT> readMap(String filename, Class<RT> clazz)
            throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, ParseException {

        try (InputStream fileIn = CurrentResourceLocator.read(filename);
                InputStreamReader inReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inReader);
                JsonParser parser = factory.createParser(bufferedReader);) {
            parser.nextToken();
            return readObjectMap(clazz, parser);
        }
    }

    public static <LT> List<LT> readList(String filename, Class<LT> clazz)
            throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, ParseException {

        try (InputStream fileIn = CurrentResourceLocator.read(filename);
                InputStreamReader inReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inReader);
                JsonParser parser = factory.createParser(bufferedReader);) {
            parser.nextToken();
            return readList(clazz, parser);
        }
    }

    private static <T> List<T> readList(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        if (clazz == null || parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new AssertionError(parser.getCurrentToken().name());
        }

        List<T> list = new ArrayList();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            list.add(readUnknownObject(clazz, parser));
        }
        return list;
    }

    private static <T> HashMap<String, T> readObjectMap(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        HashMap<String, T> map = new HashMap();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String headerField = parser.getCurrentName();
            parser.nextToken();
            map.put(headerField, readUnknownObject(clazz, parser));
        }
        return map;
    }

    private static <T> HashMap<String, HashMap<String, T>> readObjectMapMap(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        HashMap<String, HashMap<String, T>> map = new HashMap();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String headerField = parser.getCurrentName();
            parser.nextToken();
            map.put(headerField, readObjectMap(clazz, parser));
        }
        return map;
    }

    private static <T> T readUnknownObject(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        switch (parser.getCurrentToken()) {
            case START_OBJECT:
                return readObject(clazz, parser);
            case VALUE_STRING:
                return readString(clazz, parser);
            case VALUE_NUMBER_INT:
                return readInt(clazz, parser);
            case VALUE_NUMBER_FLOAT:
                return readFloat(clazz, parser);
            case VALUE_TRUE:
                return readBool(clazz, parser, Boolean.TRUE);
            case VALUE_FALSE:
                return readBool(clazz, parser, Boolean.FALSE);
            case VALUE_NULL:
                return null;
            default:
                throw new AssertionError(parser.getCurrentToken().name());
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        Class<?> tmpClass = clazz;
        do {
            try {
                Field f = tmpClass.getDeclaredField(fieldName);
                return f;
            } catch (NoSuchFieldException e) {
                tmpClass = tmpClass.getSuperclass();
            }
        } while (tmpClass != null);

        throw new RuntimeException("Field '" + fieldName + "' not found on class " + clazz);
    }

    private static <T> T readObject(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        T object = clazz.newInstance();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String headerField = parser.getCurrentName();
            parser.nextToken();

            Field field = getDeclaredField(clazz, headerField);
            field.setAccessible(true);

            JacksonList listAnnotation = (JacksonList) field.getAnnotation(JacksonList.class);
            if (listAnnotation != null) {
                field.set(object, readList(listAnnotation.value(), parser));
                continue;
            }

            JacksonMapMap mapMapAnnotation = (JacksonMapMap) field.getAnnotation(JacksonMapMap.class);
            if (mapMapAnnotation != null) {
                field.set(object, readObjectMapMap(mapMapAnnotation.value(), parser));
                continue;
            }

            JacksonMap mapAnnotation = (JacksonMap) field.getAnnotation(JacksonMap.class);
            if (mapAnnotation != null) {
                field.set(object, readObjectMap(mapAnnotation.value(), parser));
                continue;
            }
            field.set(object, readUnknownObject(field.getType(), parser));
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private static <T> T readString(Class<T> clazz, JsonParser parser) throws IOException, NumberFormatException, AssertionError {
        if (clazz.isEnum()) {
            String name = parser.getText();
            try {
                return (T) Enum.valueOf((Class<Enum>) clazz, name);
            } catch (IllegalArgumentException ex) {
                for (Enum e : ((Class<Enum>) clazz).getEnumConstants()) {
                    if (e.name().compareToIgnoreCase(name) == 0) {
                        return (T) e;
                    }
                }
                throw new IllegalArgumentException("No enum constant " + clazz.getCanonicalName() + "." + name);
            }
        } else if (clazz.equals(String.class)) {
            return (T) parser.getText();
        } else if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(parser.getText());
        } else if (clazz.equals(Float.class)) {
            return (T) Float.valueOf(parser.getText());
        } else if (clazz.equals(Boolean.class)) {
            String text = parser.getText();
            return (T) ("1".equals(text) ? Boolean.valueOf(true) : Boolean
                    .valueOf(text));
        }

        try {
            Constructor<T> ctor = clazz.getConstructor(String.class);
            ctor.setAccessible(true);
            return ctor.newInstance(parser.getText());
        } catch (NoSuchMethodException e) {
            throw new AssertionError("No String constructor available for " + clazz.getSimpleName(), e);
        } catch (Exception e) {
            throw new AssertionError("Error trying to call String constructor for " + clazz.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T readInt(Class<T> clazz, JsonParser parser) throws IOException, AssertionError {
        if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(parser.getIntValue());
        }
        if (clazz.equals(Float.class)) {
            return (T) Float.valueOf(parser.getIntValue());
        }
        if (clazz.equals(String.class)) {
            return (T) String.valueOf(parser.getIntValue());
        }
        throw new AssertionError(parser.getCurrentToken().name());
    }

    @SuppressWarnings("unchecked")
    private static <T> T readFloat(Class<T> clazz, JsonParser parser) throws AssertionError, IOException {
        if (clazz.equals(Float.class)) {
            return (T) Float.valueOf(parser.getFloatValue());
        }
        if (clazz.equals(String.class)) {
            return (T) String.valueOf(parser.getFloatValue());
        }
        throw new AssertionError(parser.getCurrentToken().name());
    }

    @SuppressWarnings("unchecked")
    private static <T> T readBool(Class<T> clazz, JsonParser parser, Boolean value) throws AssertionError {
        if (clazz.equals(Boolean.class)) {
            return (T) value;
        }
        if (clazz.equals(String.class)) {
            return (T) String.valueOf(value);
        }
        throw new AssertionError(parser.getCurrentToken().name());
    }
}
