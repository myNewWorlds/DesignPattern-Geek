package com.ljt.ratelimiter.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class MappingClass4Test {

    List<MappingClassUnit4Test> persons;

    public void setPersons(List<MappingClassUnit4Test> persons) {
        this.persons = persons;
    }

    @Getter
    public static class MappingClassUnit4Test{

        private String name;
        private int age;
        private boolean male;

        @Override
        public String toString() {
            return "MappingClassUnit4Test{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", male=" + male +
                    '}';
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setMale(boolean male) {
            this.male = male;
        }
    }
}
