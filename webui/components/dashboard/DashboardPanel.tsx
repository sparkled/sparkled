import { Card, Chip, Skeleton } from '@heroui/react'
import NextLink from 'next/link'
import { ReactNode } from 'react'

export type DashboardPanelProps<TItem> = {
  title: string
  icon: ReactNode
  count?: number
  viewAllHref: string
  items: TItem[]
  renderItem: (item: TItem) => ReactNode
  isLoading?: boolean
  emptyLabel: string
  addAction?: ReactNode
}

export function DashboardPanel<TItem>({
  title,
  icon,
  count,
  viewAllHref,
  items,
  renderItem,
  isLoading,
  emptyLabel,
  addAction,
}: DashboardPanelProps<TItem>) {
  return (
    <Card className='flex h-full flex-col'>
      <Card.Header className='flex flex-row items-center justify-between gap-2'>
        <div className='flex items-center gap-2'>
          <span className='text-accent'>{icon}</span>
          <Card.Title className='text-base'>{title}</Card.Title>
          {isLoading ? (
            <Skeleton className='h-6 w-8 rounded-full' />
          ) : (
            <Chip color='accent' size='sm'>
              {count ?? 0}
            </Chip>
          )}
        </div>
        {addAction}
      </Card.Header>
      <Card.Content className='flex-1'>
        {isLoading ? (
          <div className='flex flex-col gap-3'>
            {Array.from({ length: 3 }).map((_, index) => (
              <Skeleton key={index} className='h-10 w-full rounded-lg' />
            ))}
          </div>
        ) : items.length === 0 ? (
          <p className='text-muted py-4 text-center text-sm'>{emptyLabel}</p>
        ) : (
          <div className='divide-separator flex flex-col divide-y'>
            {items.map((item, index) => (
              <div key={index}>{renderItem(item)}</div>
            ))}
          </div>
        )}
      </Card.Content>
      <Card.Footer>
        <NextLink className='text-accent text-sm hover:underline' href={viewAllHref}>
          View all
        </NextLink>
      </Card.Footer>
    </Card>
  )
}
