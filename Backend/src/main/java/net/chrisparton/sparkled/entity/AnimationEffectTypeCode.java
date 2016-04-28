package net.chrisparton.sparkled.entity;

import net.chrisparton.sparkled.renderer.effect.EffectRenderer;
import net.chrisparton.sparkled.renderer.effect.FlashEffectRenderer;
import net.chrisparton.sparkled.renderer.effect.LineLeftEffectRenderer;
import net.chrisparton.sparkled.renderer.effect.LineRightEffectRenderer;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum AnimationEffectTypeCode {

    FLASH(
            new AnimationEffectType(
                    "FLASH",
                    "Flash",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new FlashEffectRenderer()
    ),
    LINE_LEFT(
            new AnimationEffectType(
                    "LINE_LEFT",
                    "Line Left",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new LineLeftEffectRenderer()
    ),
    LINE_RIGHT(
            new AnimationEffectType(
                    "LINE_RIGHT",
                    "Line Right",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new LineRightEffectRenderer()
    );

    public static final List<AnimationEffectType> EFFECT_TYPES = Arrays.asList(AnimationEffectTypeCode.values())
            .stream()
            .map(AnimationEffectTypeCode::getEffectType)
            .collect(toList());

    private AnimationEffectType effectType;
    private EffectRenderer renderer;

    AnimationEffectTypeCode(AnimationEffectType effectType, EffectRenderer renderer) {
        this.effectType = effectType;
        this.renderer = renderer;
    }

    public AnimationEffectType getEffectType() {
        return effectType;
    }

    public EffectRenderer getRenderer() {
        return renderer;
    }
}