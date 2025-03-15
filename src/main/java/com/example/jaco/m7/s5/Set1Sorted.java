/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s5;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * ConcurrentSkipListSet is-a NavigableSet
 */
public class Set1Sorted {
    public static void main(String[] args) {
        NavigableSet<String> friends = new ConcurrentSkipListSet<>();

        friends.add("Tom");
        friends.add("Zoe");
        friends.add("Bob");
        friends.add("Kim");

        System.out.println("Sorted set of friends: " + friends);
    }
}
