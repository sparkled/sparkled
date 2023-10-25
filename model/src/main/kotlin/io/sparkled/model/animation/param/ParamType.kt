package io.sparkled.model.animation.param

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
enum class ParamType {

    /**
     * Indicates that no type has been provided.
     */
    @JsonEnumDefaultValue
    NONE,

    /**
     * True or false.
     */
    BOOLEAN,

    /**
     * A single color value.
     */
    COLOR,

    /**
     * Multiple color values.
     */
    COLORS,

    /**
     * A number, either whole or fractional.
     */
    DECIMAL,

    /**
     * A whole number.
     */
    INTEGER,

    /**
     * A string.
     */
    STRING
}
