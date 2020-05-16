package org.uma.jmetal.bzu.utils;

import java.io.Serializable;

public class IntRange implements Serializable{

    int min,max;

    /**
     * Initializes a new instance of the IntRange class.
     */
    public IntRange() {
    }

    /**
     * Initializes a new instance of the IntRange class.
     * @param min Minimum value of the range.
     * @param max Maximum value of the range.
     */
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Get minimum value of the range.
     * @return Minimum value.
     */
    public int getMin() {
        return min;
    }

    /**
     * Set minimum value of the range.
     * @param min Minimum value.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get Maximum value of the range.
     * @return Maximum value.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set maximum value of the range.
     * @param max Maximum value.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Length of the range (difference between maximum and minimum values).
     * @return Length.
     */
    public double length(){
        return max - min;
    }

    /**
     * Check if the specified range is inside of the range.
     * @param x Value.
     * @return True if the value is inside of the range, otherwise returns false.
     */
    public boolean isInside(int x){
        return (x >= min) && (x <= max);
    }

    /**
     * Check if the specified range overlaps with the range.
     * @param range IntRange.
     * @return True if the range overlaps with the range, otherwise returns false.
     */
    public boolean IsOverlapping( IntRange range ){
        return ( ( isInside( range.min ) ) || ( isInside( range.max ) ) ||
                ( range.isInside( min ) ) || ( range.isInside( max ) ) );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().isAssignableFrom(IntRange.class)) {
            IntRange range = (IntRange)obj;
            return this.min == range.getMin() && this.max == range.getMax();
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.min;
        hash = 71 * hash + this.max;
        return hash;
    }

    @Override
    public String toString() {
        return "Minimum: " + this.min + " Maximum: " + this.max;
    }

}
