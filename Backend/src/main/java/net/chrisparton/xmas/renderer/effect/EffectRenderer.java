package net.chrisparton.xmas.renderer.effect;

import net.chrisparton.xmas.entity.AnimationEffect;
import net.chrisparton.xmas.entity.AnimationEffectChannel;
import net.chrisparton.xmas.renderer.data.AnimationFrame;

public abstract class EffectRenderer {

    public abstract void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect);
}
