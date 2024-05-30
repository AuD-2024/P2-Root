package p2.gui;

/**
 * An interface for animations that can be visualized and stopped.
 * The implementations of this interface are responsible for calling {@link Object#wait()} to wait for the next step
 * and update the visualization before waiting.
 * To avoid blocking the JavaFX thread, any interactions should be performed in  a new thread.
 * To continue the animation, {@code getSyncObject().notify()} should be called from the JavaFX thread.
 */
public interface Animation {

    /**
     * Returns the object that is used for synchronization.
     * This object is used to call {@link Object#wait()} and {@link Object#notify()}.
     * Calling {@link Object#notify()} on this object should be done from the JavaFX thread and causes the animation to execute the next step.
     *
     * @return the object that is used for synchronization
     */
    Object getSyncObject();

    /**
     * Tells the animation to finish gracefully independent of the current state.
     */
    void finishWithNextStep();

    /**
     * Tells the animation that it does not need to finish with the next step anymore.
     */
    void disableFinishWithNextStep();

    /**
     * @return {@code true} if the animation should finish with the next step, {@code false} otherwise
     */
    boolean isFinishingWithNextStep();

    default void waitUntilNextStep() {
        if (!isFinishingWithNextStep()) {
            synchronized (getSyncObject()) {
                try {
                    getSyncObject().wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
