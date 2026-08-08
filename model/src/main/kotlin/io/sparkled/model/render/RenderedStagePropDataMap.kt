package io.sparkled.model.render

import io.sparkled.model.annotation.GenerateClientType

/**
 * A map of (stage prop ID or stage prop group code) to rendered stage prop data.
 */
@GenerateClientType
class RenderedStagePropDataMap : HashMap<String, RenderedStagePropData>()
