'use client'

import { useApiGetSetting, useApiUpdateSetting } from '@/hooks/api/useApi'
import { Button, Popover, Skeleton, Slider, ToggleButton, ToggleButtonGroup } from '@heroui/react'
import { Settings } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Key } from 'react-aria-components'

const BRIGHTNESS_SETTING_ID = 'BRIGHTNESS'
const MIN_BRIGHTNESS = 0
const MAX_BRIGHTNESS = 255

const FRAMES_PER_SECOND_SETTING_ID = 'FRAMES_PER_SECOND'
const DEFAULT_FRAMES_PER_SECOND = 60
const FRAMES_PER_SECOND_VALUES = [15, 30, 45, 60]

function toPercent(brightness: number): number {
  return Math.round((brightness / MAX_BRIGHTNESS) * 100)
}

function toSingleValue(value: number | number[]): number {
  return Array.isArray(value) ? value[0] : value
}

function firstKey(keys: Set<Key>): Key | undefined {
  return Array.from(keys)[0]
}

export function SettingsControl() {
  const { data: brightnessSetting, isLoading: isBrightnessLoading } =
    useApiGetSetting(BRIGHTNESS_SETTING_ID)
  const { data: framesPerSecondSetting, isLoading: isFramesPerSecondLoading } = useApiGetSetting(
    FRAMES_PER_SECOND_SETTING_ID,
  )
  const { trigger: updateSetting } = useApiUpdateSetting()

  const [brightness, setBrightness] = useState(MAX_BRIGHTNESS)
  const [framesPerSecond, setFramesPerSecond] = useState(DEFAULT_FRAMES_PER_SECOND)

  useEffect(() => {
    if (brightnessSetting) {
      setBrightness(Number(brightnessSetting.value))
    }
  }, [brightnessSetting])

  useEffect(() => {
    if (framesPerSecondSetting) {
      setFramesPerSecond(Number(framesPerSecondSetting.value))
    }
  }, [framesPerSecondSetting])

  if (isBrightnessLoading || isFramesPerSecondLoading) {
    return <Skeleton className='h-9 w-9 rounded-full' />
  }

  return (
    <Popover>
      <Button isIconOnly aria-label='Settings' variant='ghost'>
        <Settings size={18} />
      </Button>
      <Popover.Content className='w-64' placement='bottom end'>
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

          <Popover.Heading className='mt-4'>Frames per second</Popover.Heading>
          <ToggleButtonGroup
            aria-label='Frames per second'
            className='mt-3'
            disallowEmptySelection
            selectedKeys={[framesPerSecond]}
            selectionMode='single'
            onSelectionChange={keys => {
              const value = firstKey(keys) as number
              setFramesPerSecond(value)
              updateSetting({
                id: FRAMES_PER_SECOND_SETTING_ID,
                setting: { value: String(value) },
              })
            }}
          >
            {FRAMES_PER_SECOND_VALUES.map(value => (
              <ToggleButton key={value} id={value} size='sm'>
                {value}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>
        </Popover.Dialog>
      </Popover.Content>
    </Popover>
  )
}
