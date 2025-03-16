/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s6;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Introduction to CopyOnWriteArraySet
 */
public class CowaSet {
    public static void main(String[] args) {
        Set<Integer> data = new CopyOnWriteArraySet<>(Set.of(1, 2, 3, 4));

        System.out.println("Original data: " + data);

        // get a snapshot of data
        Iterator<Integer> it = data.iterator();
        // change data
        data.remove(3);
        data.add(33);
        data.add(5);
        System.out.println("The current data: " + data);

        System.out.print("Reading from the 'old' snapshot: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // get a new snapshot of data
        it = data.iterator();

        it.next();
        try {
            // disabled
            it.remove();
        } catch (UnsupportedOperationException ex) {
            System.out.println("Can't remove from iterator: " + ex);
        }
    }
}
