package de.hochschuletrier.gdw.examples.jackson;

import de.hochschuletrier.gdw.commons.jackson.JacksonList;
import de.hochschuletrier.gdw.commons.jackson.JacksonMap;
import java.util.List;
import java.util.Map;

/**
 * An example object for the Jackson reader/writer
 *
 * @author Santo Pfingsten
 */
public class JacksonObjectExample {

    public String name;
    public Integer age;
    public Float progress;
    public Boolean master;
    public Gender gender;
    public Course specialCourse;
    @JacksonList(Course.class)
    public List<Course> courses;
    @JacksonMap(Course.class)
    public Map<String, Course> map;

    public static class Course {

        public Course() {
        }

        public Course(String name, int tries) {
            this.name = name;
            this.tries = tries;
        }
        public String name;
        public Integer tries;
    }

    public static enum Gender {

        MALE, FEMALE;
    }
}
