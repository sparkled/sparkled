package net.chrisparton.sparkled.converter;

import net.chrisparton.sparkled.model.animation.AnimationEffect;

public abstract class EffectTypeParamConverter<T> {

    public abstract T convert(AnimationEffect effect);
}
