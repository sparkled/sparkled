'use client'

import { FieldError, Input, Label, TextField, ToggleButton, ToggleButtonGroup } from '@heroui/react'
import { useEffect, useState } from 'react'
import { Key } from 'react-aria-components'
import { CronFrequency, DAYS_OF_WEEK, MONTHS, getSummary, parseCronExpression } from '@/utils/cron'

export type CronBuilderProps = {
  value: string
  onChange: (value: string) => void
}

type Frequency = CronFrequency

const FREQUENCIES: { value: Frequency; label: string }[] = [
  { value: 'NEVER', label: 'Never' },
  { value: 'HOURLY', label: 'Hourly' },
  { value: 'DAILY', label: 'Daily' },
  { value: 'WEEKLY', label: 'Weekly' },
  { value: 'MONTHLY', label: 'Monthly' },
  { value: 'YEARLY', label: 'Yearly' },
  { value: 'CUSTOM', label: 'Custom' },
]

const HOURS = Array.from({ length: 24 }, (_, hour) => hour)
const COMMON_MINUTES = [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55]
const DAYS_OF_MONTH = Array.from({ length: 31 }, (_, day) => day + 1)

function pad(value: number): string {
  return value.toString().padStart(2, '0')
}

function firstKey(keys: Set<Key>): Key | undefined {
  return Array.from(keys)[0]
}

function buildCronExpression(
  frequency: Frequency,
  hour: number,
  minute: number,
  daysOfWeek: string[],
  dayOfMonth: number,
  month: number,
): string {
  switch (frequency) {
    case 'HOURLY':
      return `0 ${minute} * * * ?`
    case 'DAILY':
      return `0 ${minute} ${hour} * * ?`
    case 'WEEKLY':
      return `0 ${minute} ${hour} ? * ${daysOfWeek.length > 0 ? daysOfWeek.join(',') : '*'}`
    case 'MONTHLY':
      return `0 ${minute} ${hour} ${dayOfMonth} * ?`
    case 'YEARLY':
      return `0 ${minute} ${hour} ${dayOfMonth} ${month} ?`
    default:
      return ''
  }
}

export function CronBuilder({ value, onChange }: CronBuilderProps) {
  const parsed = value ? parseCronExpression(value) : null

  const [frequency, setFrequency] = useState<Frequency>(
    parsed ? parsed.frequency : value ? 'CUSTOM' : 'WEEKLY',
  )
  const [daysOfWeek, setDaysOfWeek] = useState<string[]>(parsed?.daysOfWeek ?? ['SUN'])
  const [hour, setHour] = useState(parsed?.hour ?? 0)
  const [minute, setMinute] = useState(parsed?.minute ?? 0)
  const [dayOfMonth, setDayOfMonth] = useState(parsed?.dayOfMonth ?? 1)
  const [month, setMonth] = useState(parsed?.month ?? 1)
  const [customCron, setCustomCron] = useState(value)

  useEffect(() => {
    if (frequency === 'NEVER') {
      onChange('')
    } else if (frequency === 'CUSTOM') {
      onChange(customCron)
    } else {
      onChange(buildCronExpression(frequency, hour, minute, daysOfWeek, dayOfMonth, month))
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [frequency, hour, minute, daysOfWeek, dayOfMonth, month, customCron])

  const showHoursAndMinutes =
    frequency === 'DAILY' ||
    frequency === 'WEEKLY' ||
    frequency === 'MONTHLY' ||
    frequency === 'YEARLY'

  return (
    <div className='flex flex-col gap-4'>
      <div className='flex flex-col gap-2'>
        <span className='text-sm font-medium'>Schedule</span>
        <ToggleButtonGroup
          aria-label='Frequency'
          disallowEmptySelection
          selectedKeys={[frequency]}
          selectionMode='single'
          onSelectionChange={keys => setFrequency(firstKey(keys) as Frequency)}
        >
          {FREQUENCIES.map(option => (
            <ToggleButton key={option.value} id={option.value} size='sm'>
              {option.label}
            </ToggleButton>
          ))}
        </ToggleButtonGroup>
      </div>

      {frequency === 'WEEKLY' && (
        <div className='flex flex-col gap-2'>
          <span className='text-sm font-medium'>Days of Week</span>
          <ToggleButtonGroup
            aria-label='Days of week'
            isDetached
            selectedKeys={daysOfWeek}
            selectionMode='multiple'
            onSelectionChange={keys => setDaysOfWeek(Array.from(keys) as string[])}
          >
            {DAYS_OF_WEEK.map(day => (
              <ToggleButton key={day.value} id={day.value} size='sm'>
                {day.label}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>
        </div>
      )}

      {frequency === 'YEARLY' && (
        <div className='flex flex-col gap-2'>
          <span className='text-sm font-medium'>Month</span>
          <ToggleButtonGroup
            aria-label='Month'
            className='grid grid-cols-6 gap-1'
            disallowEmptySelection
            isDetached
            selectedKeys={[month]}
            selectionMode='single'
            onSelectionChange={keys => setMonth(firstKey(keys) as number)}
          >
            {MONTHS.map(option => (
              <ToggleButton key={option.value} id={option.value} size='sm'>
                {option.label}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>
        </div>
      )}

      {(frequency === 'MONTHLY' || frequency === 'YEARLY') && (
        <div className='flex flex-col gap-2'>
          <span className='text-sm font-medium'>Day of Month</span>
          <ToggleButtonGroup
            aria-label='Day of month'
            className='grid grid-cols-7 gap-1'
            disallowEmptySelection
            isDetached
            selectedKeys={[dayOfMonth]}
            selectionMode='single'
            onSelectionChange={keys => setDayOfMonth(firstKey(keys) as number)}
          >
            {DAYS_OF_MONTH.map(day => (
              <ToggleButton key={day} id={day} size='sm'>
                {pad(day)}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>
        </div>
      )}

      {showHoursAndMinutes && (
        <div className='flex flex-col gap-4 sm:flex-row sm:gap-8'>
          <div className='flex flex-col gap-2'>
            <span className='text-sm font-medium'>Hours</span>
            <ToggleButtonGroup
              aria-label='Hour'
              className='grid grid-cols-6 gap-1'
              disallowEmptySelection
              isDetached
              selectedKeys={[hour]}
              selectionMode='single'
              onSelectionChange={keys => setHour(firstKey(keys) as number)}
            >
              {HOURS.map(option => (
                <ToggleButton key={option} id={option} size='sm'>
                  {pad(option)}
                </ToggleButton>
              ))}
            </ToggleButtonGroup>
          </div>

          <div className='flex flex-col gap-2'>
            <span className='text-sm font-medium'>Minutes</span>
            <ToggleButtonGroup
              aria-label='Minute'
              className='grid grid-cols-6 gap-1'
              disallowEmptySelection
              isDetached
              selectedKeys={[minute]}
              selectionMode='single'
              onSelectionChange={keys => setMinute(firstKey(keys) as number)}
            >
              {COMMON_MINUTES.map(option => (
                <ToggleButton key={option} id={option} size='sm'>
                  {pad(option)}
                </ToggleButton>
              ))}
            </ToggleButtonGroup>
          </div>
        </div>
      )}

      {frequency === 'HOURLY' && (
        <div className='flex flex-col gap-2'>
          <span className='text-sm font-medium'>Minutes</span>
          <ToggleButtonGroup
            aria-label='Minute'
            className='grid grid-cols-6 gap-1'
            disallowEmptySelection
            isDetached
            selectedKeys={[minute]}
            selectionMode='single'
            onSelectionChange={keys => setMinute(firstKey(keys) as number)}
          >
            {COMMON_MINUTES.map(option => (
              <ToggleButton key={option} id={option} size='sm'>
                {pad(option)}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>
        </div>
      )}

      {frequency === 'CUSTOM' && (
        <TextField isRequired name='cronExpression' value={customCron} onChange={setCustomCron}>
          <Label>Cron expression</Label>
          <Input placeholder='0 0 * * * ?' />
          <FieldError />
        </TextField>
      )}

      {frequency === 'NEVER' && (
        <p className='text-muted text-sm'>This task will not run automatically.</p>
      )}

      {frequency !== 'NEVER' && frequency !== 'CUSTOM' && (
        <div className='border-border bg-surface flex flex-col gap-1 rounded-lg border px-3 py-2 text-sm sm:flex-row sm:items-center sm:justify-between'>
          <span>{getSummary(frequency, hour, minute, daysOfWeek, dayOfMonth, month)}</span>
          <code className='text-muted font-mono text-xs'>
            {buildCronExpression(frequency, hour, minute, daysOfWeek, dayOfMonth, month)}
          </code>
        </div>
      )}
    </div>
  )
}
