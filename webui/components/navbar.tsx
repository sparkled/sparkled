'use client'

import { BrightnessControl } from '@/components/dashboard/BrightnessControl'
import { ThemeSwitch } from '@/components/theme-switch'
import { siteConfig } from '@/config/site'
import { Button, InputGroup, Kbd, Link, TextField } from '@heroui/react'
import clsx from 'clsx'
import { SearchIcon } from 'lucide-react'
import NextLink from 'next/link'
import { useState } from 'react'

export const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false)

  const searchInput = (
    <TextField aria-label='Search' type='search'>
      <InputGroup>
        <InputGroup.Prefix>
          <SearchIcon />
        </InputGroup.Prefix>
        <InputGroup.Input className='text-sm' placeholder='Search...' />
        <InputGroup.Suffix>
          <Kbd className='hidden lg:inline-flex'>
            <Kbd.Abbr keyValue='command' />
            <Kbd.Content>K</Kbd.Content>
          </Kbd>
        </InputGroup.Suffix>
      </InputGroup>
    </TextField>
  )

  return (
    <nav className='border-separator bg-background/70 sticky top-0 z-40 w-full border-b backdrop-blur-lg'>
      <header className='mx-auto flex h-16 max-w-[1280px] items-center justify-between gap-4 px-6'>
        <div className='flex items-center gap-4'>
          <NextLink className='flex items-center gap-1' href='/'>
            {/* TODO Sparkled logo */}
            <p className='font-bold text-inherit'>{siteConfig.name}</p>
          </NextLink>
        </div>

        <div className='flex items-center gap-2'>
          <BrightnessControl />
        </div>
      </header>

      {isMenuOpen && (
        <div className='border-separator border-t sm:hidden'>
          <div className='p-4'>{searchInput}</div>
          <ul className='flex flex-col gap-2 px-4 pb-4'>
            {siteConfig.navMenuItems.map((item, index) => (
              <li key={`${item.label}-${index}`}>
                <Link
                  className={clsx(
                    'block py-2 text-lg no-underline',
                    index === 2
                      ? 'text-accent'
                      : index === siteConfig.navMenuItems.length - 1
                        ? 'text-danger'
                        : 'text-foreground',
                  )}
                  href='#'
                >
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      )}
    </nav>
  )
}
