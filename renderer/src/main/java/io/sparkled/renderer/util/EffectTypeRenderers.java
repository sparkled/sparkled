package io.sparkled.renderer.util;

import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.renderer.effect.EffectRenderer;
import io.sparkled.renderer.effect.FlashEffectRenderer;
import io.sparkled.renderer.effect.LineEffectRenderer;
import io.sparkled.renderer.effect.SplitLineEffectRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EffectTypeRenderers {

    private static Map<EffectTypeCode, Supplier<EffectRenderer>> RENDERERS = new HashMap<>();

    static {
        RENDERERS.put(EffectTypeCode.FLASH, FlashEffectRenderer::new);
        RENDERERS.put(EffectTypeCode.LINE, LineEffectRenderer::new);
        RENDERERS.put(EffectTypeCode.SPLIT_LINE, SplitLineEffectRenderer::new);
    }

    private EffectTypeRenderers() {
    }

    public static EffectRenderer get(EffectTypeCode code) {
        Supplier<EffectRenderer> supplier = RENDERERS.get(code);
        return supplier.get();
    }
}
