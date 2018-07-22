package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedChannelData;
import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SaveRenderedChannelsQuery implements PersistenceQuery<Integer> {

    private static final Logger logger = Logger.getLogger(SaveRenderedChannelsQuery.class.getName());

    private final Song song;
    private final RenderedChannelMap renderedChannelMap;

    public SaveRenderedChannelsQuery(Song song, RenderedChannelMap renderedChannelMap) {
        this.song = song;
        this.renderedChannelMap = renderedChannelMap;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        AtomicInteger recordsSaved = new AtomicInteger(0);
        renderedChannelMap.forEach((key, value) -> {
            RenderedChannelData renderedChannelData = new RenderedChannelData();
            renderedChannelData.setSongId(song.getId()).setChannelCode(key).setLedCount(value.getLedCount()).setData(value.getData());
            entityManager.merge(renderedChannelData);
            recordsSaved.incrementAndGet();
        });

        logger.info("Saved " + recordsSaved.get() + " RenderedChannelData record(s) for song " + song.getId());
        return recordsSaved.get();
    }
}
