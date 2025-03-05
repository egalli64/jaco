/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s5;

/**
 * The product for Producer / Consumer
 */
class Product {
    private int id;
    private int value;
    /** a product could be produced or consumed */
    private boolean consumed;

    /**
     * A new product is in non-produced (consumed) state
     */
    public Product() {
        this.consumed = true;
    }

    /**
     * Getter
     * 
     * @return true if the product is ready to be consumed
     */
    public boolean isProduced() {
        return !consumed;
    }

    /**
     * Getter
     * 
     * @return true if the product is ready to be produced
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Produce the product
     * 
     * @param id    the new id
     * @param value the new value
     * @throws IllegalStateException if trying to produce a non-consumed product
     */
    public void produce(int id, int value) {
        if (!consumed) {
            throw new IllegalStateException("Can't overwrite a non-consumed product");
        }

        this.id = id;
        this.value = value;
        consumed = false;
    }

    /**
     * Consume the product
     * 
     * @return the value
     * @throws IllegalStateException if trying to consume a non-produced product
     */
    public int consume() {
        if (consumed) {
            throw new IllegalStateException("Can't consume a non-produced product");
        }

        consumed = true;
        return value;
    }

    @Override
    public String toString() {
        return "{" + id + ", " + value + "}";
    }
}
