package net.chrisparton.sparkled.model.animation.effect.reference;

import net.chrisparton.sparkled.model.animation.effect.EffectType;
import net.chrisparton.sparkled.model.animation.effect.EffectTypeCode;
import net.chrisparton.sparkled.model.animation.param.Param;
import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.model.animation.param.ParamType;

import java.util.Arrays;
import java.util.List;

public class EffectTypes {
    private static final List<EffectType> TYPES = Arrays.asList(
            effectType(EffectTypeCode.FLASH, "Flash"),
            effectType(EffectTypeCode.LINE, "Line",
                    param(ParamName.LENGTH, ParamType.INTEGER)
            )
    );

    public static List<EffectType> get() {
        return TYPES;
    }

    private static EffectType effectType(EffectTypeCode effectType, String name, Param... params) {
        return new EffectType().setCode(effectType).setName(name).setParams(Arrays.asList(params));
    }

    private static Param param(ParamName paramName, ParamType type) {
        return new Param().setName(paramName.getName()).setType(type);
    }
}