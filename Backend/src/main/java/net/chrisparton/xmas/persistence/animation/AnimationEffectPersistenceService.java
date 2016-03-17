package net.chrisparton.xmas.persistence.animation;

import net.chrisparton.xmas.entity.AnimationEffect;
import net.chrisparton.xmas.entity.AnimationEffect_;
import net.chrisparton.xmas.persistence.PersistenceService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AnimationEffectPersistenceService {

    public List<AnimationEffect> getAllAnimationEffects() {
        return PersistenceService.instance().perform(this::getAllAnimationEffects);
    }

    private List<AnimationEffect> getAllAnimationEffects(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AnimationEffect> cq = cb.createQuery(AnimationEffect.class);
        Root<AnimationEffect> animationEffectRoot = cq.from(AnimationEffect.class);

        cq.orderBy(
                cb.asc(animationEffectRoot.get(AnimationEffect_.name))
        );

        TypedQuery<AnimationEffect> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
