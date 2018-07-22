package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedChannelData;
import io.sparkled.model.entity.RenderedChannelData_;
import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedChannel;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetRenderedChannelsQuery implements PersistenceQuery<RenderedChannelMap> {

    private final Song song;

    public GetRenderedChannelsQuery(Song song) {
        this.song = song;
    }

    @Override
    public RenderedChannelMap perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RenderedChannelData> cq = cb.createQuery(RenderedChannelData.class);
        Root<RenderedChannelData> renderedSong = cq.from(RenderedChannelData.class);
        cq.where(
                cb.equal(renderedSong.get(RenderedChannelData_.songId), song.getId())
        );

        TypedQuery<RenderedChannelData> query = entityManager.createQuery(cq);

        RenderedChannelMap renderedChannelMap = new RenderedChannelMap();
        query.getResultList().forEach(renderedChannelData -> {
            renderedChannelMap.put(renderedChannelData.getChannelCode(), new RenderedChannel(
                    0,
                    song.getDurationFrames() - 1,
                    renderedChannelData.getLedCount(),
                    renderedChannelData.getData()
            ));
        });
        return renderedChannelMap;
    }
}
