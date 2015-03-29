package de.hochschuletrier.gdw.examples.jackson;

import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.jackson.JacksonWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An example use of the Jackson reader/writer
 *
 * @author Santo Pfingsten
 */
public class JacksonExample {

    public static void main(String[] args) {
        testWrite();
        testRead();
    }

    private static void testWrite() {
        JacksonObjectExample student = new JacksonObjectExample();
        student.intP = 1;
        student.longP = 2;
        student.shortP = 3;
        student.byteP = 4;
        student.charP = '5';
        student.floatP = 6;
        student.doubleP = 7;
        student.booleanP = true;
    
        student.name = "Doofus";
        student.age = 24;
        student.gender = JacksonObjectExample.Gender.MALE;
        student.master = false;
        student.progress = 0.5f;
        student.courses = new ArrayList();
        student.courses.add(new JacksonObjectExample.Course("Android", 1));
        student.courses.add(new JacksonObjectExample.Course("C++", 2));
        student.courses.add(new JacksonObjectExample.Course("Webtech", 3));
        student.specialCourse = new JacksonObjectExample.Course("Seminar", 4);
        student.map = new HashMap();
        student.map.put("Android", new JacksonObjectExample.Course("Android", 1));
        student.map.put("C++", new JacksonObjectExample.Course("C++", 2));
        student.map.put("Webtech", new JacksonObjectExample.Course("Webtech", 3));

        try {
            JacksonWriter.write("target/test.json", student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testRead() {
        try {
            JacksonObjectExample student = JacksonReader.read("target/test.json",
                    JacksonObjectExample.class);
            
            System.out.println(student.intP);
            System.out.println(student.longP);
            System.out.println(student.shortP);
            System.out.println(student.byteP);
            System.out.println(student.charP);
            System.out.println(student.floatP);
            System.out.println(student.doubleP);
            System.out.println(student.booleanP);
            
            System.out.println(student.name);
            System.out.println(student.age);
            System.out.println(student.gender);
            System.out.println(student.master);
            System.out.println(student.progress);
            if (student.courses == null) {
                System.out.println("null courses");
            } else {
                for (JacksonObjectExample.Course course : student.courses) {
                    System.out.printf("Course: %s (%d)\n", course.name, course.tries);
                }
                if (student.specialCourse != null) {
                    System.out.printf("Special course: %s (%d)\n", student.specialCourse.name, student.specialCourse.tries);
                }
            }
            if (student.map == null) {
                System.out.println("null map");
            } else {
                System.out.println("Map:");
                for (Map.Entry<String, JacksonObjectExample.Course> entry : student.map.entrySet()) {
                    System.out.printf("- Course: %s => %s (%d)\n", entry.getKey(), entry.getValue().name, entry.getValue().tries);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
