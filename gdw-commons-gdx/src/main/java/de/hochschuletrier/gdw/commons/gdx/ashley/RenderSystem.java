package de.hochschuletrier.gdw.commons.gdx.ashley;

/**
 *
 * @author Santo Pfingsten
 */
public class RenderSystem {

    /**
     * Use this to set the priority of the system. Lower means it'll get
     * executed first.
     */
    public int priority;

    private boolean processing;

    /**
     * Default constructor that will initialise an EntitySystem with priority 0.
     */
    public RenderSystem() {
        this(0);
    }

    /**
     * Initialises the EntitySystem with the priority specified.
     *
     * @param priority The priority to execute this system with (lower means
     * higher priority).
     */
    public RenderSystem(int priority) {
        this.priority = priority;
        this.processing = true;
    }

    /**
     * Called when this EntitySystem is added to an {@link RenderEngine}.
     *
     * @param engine The {@link RenderEngine} this system was added to.
     */
    public void addedToEngine(RenderEngine engine) {
    }

    /**
     * Called when this EntitySystem is removed from an {@link RenderEngine}.
     *
     * @param engine The {@link RenderEngine} the system was removed from.
     */
    public void removedFromEngine(RenderEngine engine) {
    }

    /**
     * The render method called every tick.
     */
    public void render() {
    }

    /**
     *
     * @return Whether or not the system should be processed.
     */
    public boolean checkProcessing() {
        return processing;
    }

    /**
     *
     * Sets whether or not the system should be processed by the {@link RenderEngine}.
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
