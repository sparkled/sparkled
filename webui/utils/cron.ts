export type CronFrequency = 'NEVER' | 'HOURLY' | 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY' | 'CUSTOM'

export const DAYS_OF_WEEK = [
  { value: 'SUN', label: 'Sun' },
  { value: 'MON', label: 'Mon' },
  { value: 'TUE', label: 'Tue' },
  { value: 'WED', label: 'Wed' },
  { value: 'THU', label: 'Thu' },
  { value: 'FRI', label: 'Fri' },
  { value: 'SAT', label: 'Sat' },
]

export const MONTHS = [
  { value: 1, label: 'Jan' },
  { value: 2, label: 'Feb' },
  { value: 3, label: 'Mar' },
  { value: 4, label: 'Apr' },
  { value: 5, label: 'May' },
  { value: 6, label: 'Jun' },
  { value: 7, label: 'Jul' },
  { value: 8, label: 'Aug' },
  { value: 9, label: 'Sep' },
  { value: 10, label: 'Oct' },
  { value: 11, label: 'Nov' },
  { value: 12, label: 'Dec' },
]

export type ParsedCron = {
  frequency: CronFrequency
  daysOfWeek: string[]
  hour: number
  minute: number
  dayOfMonth: number
  month: number
}

export function pad(value: number): string {
  return value.toString().padStart(2, '0')
}

export function formatTime(hour: number, minute: number): string {
  const period = hour < 12 ? 'AM' : 'PM'
  const displayHour = hour % 12 === 0 ? 12 : hour % 12
  return `${displayHour}:${pad(minute)} ${period}`
}

export function parseCronExpression(cron: string): ParsedCron | null {
  const parts = cron.trim().split(/\s+/)
  if (parts.length !== 6) {
    return null
  }

  const [sec, minField, hourField, domField, monthField, dowField] = parts
  if (sec !== '0') {
    return null
  }

  const minute = Number(minField)
  if (!Number.isInteger(minute)) {
    return null
  }

  if (hourField === '*' && domField === '*' && monthField === '*' && dowField === '?') {
    return { frequency: 'HOURLY', daysOfWeek: ['SUN'], hour: 0, minute, dayOfMonth: 1, month: 1 }
  }

  const hour = Number(hourField)
  if (!Number.isInteger(hour)) {
    return null
  }

  if (domField === '*' && monthField === '*' && dowField === '?') {
    return { frequency: 'DAILY', daysOfWeek: ['SUN'], hour, minute, dayOfMonth: 1, month: 1 }
  }

  if (domField === '?' && monthField === '*') {
    const daysOfWeek = dowField === '*' ? [] : dowField.split(',')
    return { frequency: 'WEEKLY', daysOfWeek, hour, minute, dayOfMonth: 1, month: 1 }
  }

  const dayOfMonth = Number(domField)
  if (!Number.isInteger(dayOfMonth)) {
    return null
  }

  if (monthField === '*' && dowField === '?') {
    return { frequency: 'MONTHLY', daysOfWeek: ['SUN'], hour, minute, dayOfMonth, month: 1 }
  }

  const month = Number(monthField)
  if (Number.isInteger(month) && dowField === '?') {
    return { frequency: 'YEARLY', daysOfWeek: ['SUN'], hour, minute, dayOfMonth, month }
  }

  return null
}

export function getSummary(
  frequency: CronFrequency,
  hour: number,
  minute: number,
  daysOfWeek: string[],
  dayOfMonth: number,
  month: number,
): string {
  switch (frequency) {
    case 'HOURLY':
      return `At ${pad(minute)} minutes past every hour`
    case 'DAILY':
      return `Every day at ${formatTime(hour, minute)}`
    case 'WEEKLY': {
      const labels =
        daysOfWeek.length > 0
          ? DAYS_OF_WEEK.filter(day => daysOfWeek.includes(day.value))
              .map(day => day.label)
              .join(', ')
          : 'every day'
      return `At ${formatTime(hour, minute)}, only on ${labels}`
    }
    case 'MONTHLY':
      return `On day ${dayOfMonth} of the month at ${formatTime(hour, minute)}`
    case 'YEARLY': {
      const monthLabel = MONTHS.find(candidate => candidate.value === month)?.label
      return `Every ${monthLabel} ${dayOfMonth} at ${formatTime(hour, minute)}`
    }
    default:
      return ''
  }
}

export function getCronSummary(cron: string): string {
  if (!cron) {
    return 'Never'
  }

  const parsed = parseCronExpression(cron)
  if (!parsed) {
    return `Custom (${cron})`
  }

  return getSummary(
    parsed.frequency,
    parsed.hour,
    parsed.minute,
    parsed.daysOfWeek,
    parsed.dayOfMonth,
    parsed.month,
  )
}
