import { ReactNode } from 'react'

export type DashboardPanelItemProps = {
  title: string
  subtitle?: string
  statusChip?: ReactNode
  actions?: ReactNode
}

export function DashboardPanelItem({
  title,
  subtitle,
  statusChip,
  actions,
}: DashboardPanelItemProps) {
  return (
    <div className='flex items-center justify-between gap-3 py-2'>
      <div className='min-w-0'>
        <div className='flex items-center gap-2'>
          <p className='text-foreground truncate text-sm font-medium'>{title}</p>
          {statusChip}
        </div>
        {subtitle && <p className='text-muted truncate text-xs'>{subtitle}</p>}
      </div>
      {actions && <div className='flex shrink-0 items-center gap-1'>{actions}</div>}
    </div>
  )
}
