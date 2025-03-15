/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s5;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * newSetFromMap() returns a Set
 */
public class Set2Unsorted {
    public static void main(String[] args) {
        Set<String> friends = Collections.newSetFromMap(new ConcurrentHashMap<>());

        friends.add("Tom");
        friends.add("Zoe");
        friends.add("Bob");
        friends.add("Kim");

        System.out.println("Set of friends: " + friends);
    }
}
