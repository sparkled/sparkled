'use client'

import { useApiGetSetting, useApiUpdateSetting } from '@/hooks/api/useApi'
import { Button, Popover, Skeleton, Slider } from '@heroui/react'
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
    return <Skeleton className='h-9 w-9 rounded-full' />
  }

  return (
    <Popover>
      <Button isIconOnly aria-label='Brightness' variant='ghost'>
        <Sun size={18} />
      </Button>
      <Popover.Content className='w-64'>
        <Popover.Dialog>
          <Popover.Heading>Brightness</Popover.Heading>
          <Slider
            aria-label='Brightness'
            className='mt-3 flex items-center gap-3'
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
            <Slider.Track className='relative'>
              <Slider.Fill />
              <Slider.Thumb />
            </Slider.Track>
            <span className='text-muted w-10 shrink-0 text-right text-sm'>
              {toPercent(brightness)}%
            </span>
          </Slider>
        </Popover.Dialog>
      </Popover.Content>
    </Popover>
  )
}
