/**
 Tween
 Name: Vincent Lam
 Description: A module for moving uiElements smoothly.
 Date: June 11, 2023
 **/
public class Tween {
    private uiElement element;
    private int totalTime;
    private int elapsedTime;
    private int startValueX;
    private int startValueY;
    private int endValueX;
    private int endValueY;
    private int easingType;

    // easing types
    static public int IN_OUT = 0;
    static public int IN = 1;
    static public int OUT = 2;

    // completion
    private boolean finished;

    /**
     * A constructor for the Tween class. Takes the uiElement, end position, total time, and the easing type.
     * @param element The uiElement to tween.
     * @param endValueX The x position for the tween to end at as an Integer.
     * @param endValueY The y position for the tween to end at as an Integer.
     * @param totalTime The total ticks the tween will take to complete as an Integer.
     * @param easingType The easing type used to determine the motion of the tween as an Integer.
     */
    Tween(uiElement element, int endValueX, int endValueY, int totalTime, int easingType) {
        this.element = element;
        this.elapsedTime = 0; // start time
        this.startValueX = element.getElementX();
        this.startValueY = element.getElementY();
        this.endValueX = endValueX;
        this.endValueY = endValueY;
        this.totalTime = totalTime;
        this.easingType = easingType;
        this.finished = false;
    }

    Tween(uiElement element, int startValueX, int startValueY, int endValueX, int endValueY, int totalTime, int easingType) {
        this.element = element;
        this.elapsedTime = 0; // start time
        this.startValueX = startValueX;
        this.startValueY = startValueY;
        this.endValueX = endValueX;
        this.endValueY = endValueY;
        this.totalTime = totalTime;
        this.easingType = easingType;
        this.finished = false;
    }

    /**
     * update
     * Advances the tween one tick forward if the tween is not complete, in the motion of its easing style.
     */

    public void update() {
        // advance tween if the required time hasn't been reached
        if (!finished) {
            if (elapsedTime < (totalTime + 1)) {
                if (easingType == OUT) {
                    element.setElementX((int) easeOutQuad(elapsedTime, startValueX, endValueX, totalTime));
                    element.setElementY((int) easeOutQuad(elapsedTime, startValueY, endValueY, totalTime));

                } else if (easingType == IN_OUT) {
                    element.setElementX((int) easeInOutQuad(elapsedTime, startValueX, endValueX, totalTime));
                    element.setElementY((int) easeInOutQuad(elapsedTime, startValueY, endValueY, totalTime));
                }
            }
            elapsedTime++;
        }
    }

    /**
     * easeOutQuad
     * A math function used to specify the rate of change. Slows down upon completion.
     * @param elapsedTime The number of times the tween has been updated.
     * @param startValue The start value of the tween.
     * @param endValue The goal value of the tween.
     * @param totalTime The number of update times for the tween to be finished.
     * @return An interpolated float value between the startValue and endValue according to its state of completion.
     */

    private float easeOutQuad(float elapsedTime, float startValue, float endValue, float totalTime) {
        if (elapsedTime == totalTime) {
            finished = true;
            return endValue;
        }

        elapsedTime = elapsedTime / totalTime;
        return -(endValue - startValue) * elapsedTime * (elapsedTime-2) + startValue;
    }

    /**
     * easeInOutQuad
     * A math function used to specify the rate of change. Speeds up at the beginning, and then slows down upon completion.
     * @param elapsedTime The number of times the tween has been updated.
     * @param startValue The start value of the tween.
     * @param endValue The goal value of the tween.
     * @param totalTime The number of update times for the tween to be finished.
     * @return An interpolated float value between the startValue and endValue according to its state of completion.
     */
    private float easeInOutQuad(float elapsedTime, float startValue, float endValue, float totalTime) {
        if (elapsedTime == totalTime) {
            finished = true;
            return endValue;
        }

        elapsedTime = elapsedTime / (totalTime / 2);
        if (elapsedTime < 1) return (endValue - startValue) / 2 * elapsedTime * elapsedTime + startValue;
        elapsedTime--;
        return -((endValue - startValue) / 2) * (elapsedTime * (elapsedTime - 2) - 1) + startValue;
    }

    /**
     * isFinished
     * Getter method for whether the current value of the tween is equal to its goal value.
     * @return True if finished, and false if not.
     */
    public boolean isFinished() {
        return finished;
    }
    public uiElement getElement() {
        return element;
    }

    public int getEndValueX() {
        return endValueX;
    }

    public int getEndValueY() {
        return endValueY;
    }

    public void finishEarly() {
        element.setElementX(endValueX);
        element.setElementY(endValueY);
        finished = true;
    }
}
