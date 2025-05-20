/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s6;

import java.util.concurrent.Phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

public class BasicPhaserUsage {
    private static final Logger log = LoggerFactory.getLogger(BasicPhaserUsage.class);

    public static void main(String[] args) {
        // create a phaser with one registered party
        Phaser phaser = new Phaser(1);

        // the phase number is zero
        log.info("The phaser hasn't trip any barrier (phase) yet, the phase number is {}", phaser.getPhase());
        // registered parties is two
        log.info("The number of required threads (parties) is {}", phaser.getRegisteredParties());
        // arrived parties is zero
        log.info("The number of parties already arrived is {}", phaser.getArrivedParties());

        // I've changed my mind, let's increase the number of registered parties
        phaser.register();
        log.info(phaser.toString());

        new Thread(() -> {
            try {
                log.info("Arrive & wait to continue");
                phaser.arriveAndAwaitAdvance();

                log.info("Arrive & deregister");
                phaser.arriveAndDeregister();
                log.info(phaser.toString());
            } catch (IllegalStateException e) {
                Thread.currentThread().interrupt();
                log.warn("Unexpected", e);
            }
        }).start();

        // run a fake task in the main thread, so that the worker kicks in
        FakeTasks.takeTime(2);

        log.info(phaser.toString());

        phaser.arriveAndAwaitAdvance();
        log.info("Next (1) phase: {}", phaser);

        phaser.arriveAndAwaitAdvance();
        log.info("Next (2) phase: {}", phaser);
    }
}
