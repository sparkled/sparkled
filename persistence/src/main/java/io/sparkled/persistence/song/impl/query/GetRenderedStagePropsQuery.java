package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.model.entity.RenderedStageProp_;
import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetRenderedStagePropsQuery implements PersistenceQuery<RenderedStagePropDataMap> {

    private final Song song;

    public GetRenderedStagePropsQuery(Song song) {
        this.song = song;
    }

    @Override
    public RenderedStagePropDataMap perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RenderedStageProp> cq = cb.createQuery(RenderedStageProp.class);
        Root<RenderedStageProp> renderedSong = cq.from(RenderedStageProp.class);
        cq.where(
                cb.equal(renderedSong.get(RenderedStageProp_.songId), song.getId())
        );

        TypedQuery<RenderedStageProp> query = entityManager.createQuery(cq);

        RenderedStagePropDataMap renderedStagePropDataMap = new RenderedStagePropDataMap();
        query.getResultList().forEach(stagePropData -> {
            renderedStagePropDataMap.put(stagePropData.getStagePropCode(), new RenderedStagePropData(
                    0,
                    song.getDurationFrames() - 1,
                    stagePropData.getLedCount(),
                    stagePropData.getData()
            ));
        });
        return renderedStagePropDataMap;
    }
}
