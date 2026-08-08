import { RenderResult } from '@/src/types/viewModels'

/**
 * View model for the (non-generated) `RenderResult` payload returned by the sequence preview endpoint.
 * Each entry in `stageProps` is keyed by stage prop *code* (not ID) - see `Renderer.render()`, which groups
 * preview output by `StagePropModel.code` - and contains base64-encoded RGB frame data, laid out as
 * `frameCount` consecutive blocks of `ledCount * 3` bytes.
 */
export type PreviewRenderResult = {
  startFrame: number
  frameCount: number
  stageProps: Record<string, { ledCount: number; data: Uint8Array }>
}

export type LedColor = [number, number, number]

export type DecodedPreviewRender = {
  startFrame: number
  frameCount: number
  stagePropFrames: Record<string, Uint8Array>
  stagePropLedCounts: Record<string, number>
}

const BYTES_PER_LED = 3

export function decodePreviewRender(result: RenderResult): DecodedPreviewRender {
  const stagePropFrames: Record<string, Uint8Array> = {}
  const stagePropLedCounts: Record<string, number> = {}

  Object.entries(result.stageProps).forEach(([stagePropCode, stageProp]) => {
    console.info(stageProp.data)
    stagePropFrames[stagePropCode] = new Uint8Array(stageProp.data)
    stagePropLedCounts[stagePropCode] = stageProp.ledCount
  })

  return {
    startFrame: result.startFrame,
    frameCount: result.frameCount,
    stagePropFrames,
    stagePropLedCounts,
  }
}

/**
 * Returns the LED colors for a stage prop (identified by its *code*, not ID) at the given frame index,
 * relative to the render's start frame.
 */
export function getStagePropFrameColors(
  render: DecodedPreviewRender,
  stagePropCode: string,
  relativeFrameIndex: number,
): LedColor[] {
  const bytes = render.stagePropFrames[stagePropCode]
  const ledCount = render.stagePropLedCounts[stagePropCode]
  if (!bytes || !ledCount || relativeFrameIndex < 0) {
    return []
  }

  const bytesPerFrame = ledCount * BYTES_PER_LED
  const offset = relativeFrameIndex * bytesPerFrame
  const colors: LedColor[] = []

  for (let led = 0; led < ledCount; led++) {
    const base = offset + led * BYTES_PER_LED
    colors.push([bytes[base] ?? 0, bytes[base + 1] ?? 0, bytes[base + 2] ?? 0])
  }

  return colors
}
