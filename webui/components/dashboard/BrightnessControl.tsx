'use client'

import { useApiGetSetting, useApiUpdateSetting } from '@/hooks/api/useApi'
import { Skeleton, Slider } from '@heroui/react'
import { Sun } from 'lucide-react'
import { useEffect, useState } from 'react'

const BRIGHTNESS_SETTING_ID = 'BRIGHTNESS'
const MIN_BRIGHTNESS = 0
const MAX_BRIGHTNESS = 255

function toPercent(brightness: number): number {
  return Math.round((brightness / MAX_BRIGHTNESS) * 100)
}

function toSingleValue(value: number | number[]): number {
  return Array.isArray(value) ? value[0] : value
}

export function BrightnessControl() {
  const { data: setting, isLoading } = useApiGetSetting(BRIGHTNESS_SETTING_ID)
  const { trigger: updateSetting } = useApiUpdateSetting()
  const [brightness, setBrightness] = useState(MAX_BRIGHTNESS)

  useEffect(() => {
    if (setting) {
      setBrightness(Number(setting.value))
    }
  }, [setting])

  if (isLoading) {
    return <Skeleton className='h-8 w-40 rounded-lg' />
  }

  return (
    <Slider
      aria-label='Brightness'
      className='flex w-48 items-center gap-3'
      maxValue={MAX_BRIGHTNESS}
      minValue={MIN_BRIGHTNESS}
      value={brightness}
      onChange={value => setBrightness(toSingleValue(value))}
      onChangeEnd={value =>
        updateSetting({
          id: BRIGHTNESS_SETTING_ID,
          setting: { value: String(toSingleValue(value)) },
        })
      }
    >
      <Sun className='text-muted shrink-0' size={18} />
      <Slider.Track className='relative'>
        <Slider.Fill />
        <Slider.Thumb />
      </Slider.Track>
      <span className='text-muted w-10 shrink-0 text-right text-sm'>{toPercent(brightness)}%</span>
    </Slider>
  )
}
