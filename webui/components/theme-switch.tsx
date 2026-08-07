import clsx from 'clsx'
import { Moon, Sun } from 'lucide-react'
import { useTheme } from 'next-themes'
import { FC, useEffect, useState } from 'react'

export interface ThemeSwitchProps {
  className?: string
}

export const ThemeSwitch: FC<ThemeSwitchProps> = ({ className }) => {
  const [isMounted, setIsMounted] = useState(false)
  const { setTheme, resolvedTheme } = useTheme()

  const isLight = resolvedTheme === 'light'

  const handleToggle = () => {
    setTheme(isLight ? 'dark' : 'light')
  }

  useEffect(() => {
    setIsMounted(true)
  }, [])

  if (!isMounted) return <div aria-hidden className='h-6 w-6' />

  return (
    <button
      aria-label={`Switch to ${isLight ? 'dark' : 'light'} mode`}
      className={clsx(
        'cursor-pointer px-px transition-opacity hover:opacity-80',
        'inline-flex items-center justify-center',
        'text-muted h-auto w-auto rounded-lg bg-transparent',
        className,
      )}
      onClick={handleToggle}
    >
      {isLight ? <Sun size={22} /> : <Moon size={22} />}
    </button>
  )
}
