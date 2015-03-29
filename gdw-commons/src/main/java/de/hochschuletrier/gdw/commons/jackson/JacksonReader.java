package de.hochschuletrier.gdw.commons.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
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

    private static SafeProperties readObjectSafeProperties(JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        SafeProperties properties = new SafeProperties();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String headerField = parser.getCurrentName();
            parser.nextToken();
            properties.setString(headerField, parser.getText());
        }
        return properties;
    }

    private static <T> T readUnknownObject(Class<T> clazz, JsonParser parser)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        switch (parser.getCurrentToken()) {
            case START_OBJECT:
                if(clazz.equals(SafeProperties.class))
                    return (T)readObjectSafeProperties(parser);
                else
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
            JsonToken token = parser.nextToken();

            Field field = getDeclaredField(clazz, headerField);
            field.setAccessible(true);
            final Class<?> type = field.getType();
            if(type.isPrimitive()) {
                if(token != JsonToken.VALUE_NULL)
                readPrimitiveField(parser, object, field, type);
            } else if (!readSpecialField(parser, object, field)) {
                field.set(object, readUnknownObject(type, parser));
            }
        }
        return object;
    }

    private static <T> void readPrimitiveField(JsonParser parser, T object, Field field, Class<?> type) throws IllegalArgumentException, IllegalAccessException, IOException {
        if (type.equals(Integer.TYPE)) {
            field.setInt(object, parser.getIntValue());
        } else if (type.equals(Long.TYPE)) {
            field.setLong(object, parser.getLongValue());
        } else if (type.equals(Short.TYPE)) {
            field.setShort(object, parser.getShortValue());
        } else if (type.equals(Byte.TYPE)) {
            field.setByte(object, parser.getByteValue());
        } else if (type.equals(Float.TYPE)) {
            field.setFloat(object, parser.getFloatValue());
        } else if (type.equals(Double.TYPE)) {
            field.setDouble(object, parser.getDoubleValue());
        } else if (type.equals(Boolean.TYPE)) {
            field.setBoolean(object, parser.getBooleanValue());
        } else if (type.equals(Character.TYPE)) {
            if(parser.getCurrentToken() == JsonToken.VALUE_STRING) {
                String value = parser.getText();
                if(value.length() != 1) {
                    throw new IllegalArgumentException("Expected single character for " + object.getClass().getSimpleName() + "." + field.getName() + ", got " + value.length() + " characters");
                }
                field.setChar(object, value.charAt(0));
            } else {
                throw new IllegalArgumentException("Expected single character for " + object.getClass().getSimpleName() + "." + field.getName() + ", got token " + parser.getCurrentToken());
            }
        }
    }

    private static <T> boolean readSpecialField(JsonParser parser, T object, Field field) throws IllegalArgumentException, IllegalAccessException, ParseException, InstantiationException, IOException, NoSuchFieldException {
        JacksonList listAnnotation = (JacksonList) field.getAnnotation(JacksonList.class);
        if (listAnnotation != null) {
            field.set(object, readList(listAnnotation.value(), parser));
            return true;
        }
        JacksonMapMap mapMapAnnotation = (JacksonMapMap) field.getAnnotation(JacksonMapMap.class);
        if (mapMapAnnotation != null) {
            field.set(object, readObjectMapMap(mapMapAnnotation.value(), parser));
            return true;
        }
        JacksonMap mapAnnotation = (JacksonMap) field.getAnnotation(JacksonMap.class);
        if (mapAnnotation != null) {
            field.set(object, readObjectMap(mapAnnotation.value(), parser));
            return true;
        }
        return false;
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
            return (T) Float.valueOf(parser.getFloatValue());
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
