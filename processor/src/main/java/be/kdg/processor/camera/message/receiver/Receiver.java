package be.kdg.processor.camera.message.receiver;

import java.util.List;

/**
 * Multi-purpose receiver, can be used to receive entities.
 *
 * @param <T> Type of of entity to be processed.
 */
public interface Receiver<T> {

    void receive(T entity);

    List<T> emptyMemoryBuffer();

    void bufferInDatabase(T entity);

    void bufferInMemory(T entity);
}

