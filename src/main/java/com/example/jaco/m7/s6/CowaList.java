/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s6;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Introduction to CopyOnWriteArrayList
 */
public class CowaList {
    public static void main(String[] args) {
        List<Integer> data = new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4));
        System.out.println("Original data: " + data);

        // get a snapshot of data
        Iterator<Integer> it = data.iterator();
        // change data
        data.set(2, 33);
        data.add(5);

        System.out.print("Reading from the 'old' snapshot: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.println("The current data: " + data);

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
