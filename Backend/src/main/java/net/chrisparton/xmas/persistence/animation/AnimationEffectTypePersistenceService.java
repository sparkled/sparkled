package net.chrisparton.xmas.persistence.animation;

import net.chrisparton.xmas.entity.AnimationEffectType;
import net.chrisparton.xmas.entity.AnimationEffectType_;
import net.chrisparton.xmas.persistence.PersistenceService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AnimationEffectTypePersistenceService {

    public List<AnimationEffectType> getAllAnimationEffectTypes() {
        return PersistenceService.instance().perform(this::getAllAnimationEffectTypes);
    }

    private List<AnimationEffectType> getAllAnimationEffectTypes(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AnimationEffectType> cq = cb.createQuery(AnimationEffectType.class);
        Root<AnimationEffectType> animationEffectRoot = cq.from(AnimationEffectType.class);

        cq.orderBy(
                cb.asc(animationEffectRoot.get(AnimationEffectType_.name))
        );

        TypedQuery<AnimationEffectType> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
