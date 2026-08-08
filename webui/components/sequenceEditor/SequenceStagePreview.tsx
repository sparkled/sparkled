'use client'

import { LedColor } from '@/src/utils/renderData'
import { computeLedPositions } from '@/src/utils/stagePropGeometry'
import { StagePropViewModel, StageViewModel } from '@/src/types/viewModels'
import { useMemo } from 'react'

export type SequenceStagePreviewProps = {
  stage: StageViewModel
  /**
   * LED colors for the current playback frame, keyed by stage prop code. The preview render endpoint
   * groups rendered LED data by stage prop code (not ID), so that's what's used to key this map too.
   */
  ledColorsByStagePropCode?: Record<string, LedColor[]>
}

const DEFAULT_LED_COLOR = '#18181b'

/**
 * A read-only preview of a stage, used by the sequence editor. Unlike `StagePropCanvas`, stage props here
 * can't be selected, moved, resized, or rotated - it's purely a visualisation of the stage layout and (when
 * available) the currently rendered LED colors.
 */
export function SequenceStagePreview({ stage, ledColorsByStagePropCode }: SequenceStagePreviewProps) {
  return (
    <div className='bg-surface-secondary relative h-full w-full overflow-hidden rounded-xl'>
      <svg
        className='h-full w-full'
        preserveAspectRatio='xMidYMid meet'
        viewBox={`0 0 ${stage.width} ${stage.height}`}
      >
        <rect
          className='fill-zinc-800 stroke-zinc-700'
          height={stage.height}
          strokeWidth={2}
          width={stage.width}
          x={0}
          y={0}
        />
        {stage.stageProps.map(stageProp => (
          <StagePropPreview
            key={stageProp.id}
            colors={ledColorsByStagePropCode?.[stageProp.code]}
            stageProp={stageProp}
          />
        ))}
      </svg>
    </div>
  )
}

function StagePropPreview({
  stageProp,
  colors,
}: {
  stageProp: StagePropViewModel
  colors?: LedColor[]
}) {
  const positions = useMemo(() => computeLedPositions(stageProp), [stageProp])

  return (
    <g>
      {positions.points.map((point, index) => {
        const color = colors?.[index]
        const fill = color ? `rgb(${color[0]}, ${color[1]}, ${color[2]})` : DEFAULT_LED_COLOR

        return (
          <circle
            key={index}
            className='stroke-zinc-600'
            cx={point.x}
            cy={point.y}
            fill={fill}
            r={4}
            strokeWidth={1}
          />
        )
      })}
    </g>
  )
}
