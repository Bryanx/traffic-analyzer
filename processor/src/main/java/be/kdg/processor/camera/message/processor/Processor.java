package be.kdg.processor.camera.message.processor;

/**
 * Multi-purpose processor, can be used to process entities.
 *
 * @param <T> Type of of entity to be processed.
 */
public interface Processor<T> {

    void process(int bufferTime);

    void validate(T entity);
}
