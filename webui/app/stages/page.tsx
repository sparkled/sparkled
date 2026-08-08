'use client'

import { AddStageModal } from '@/components/dashboard/AddStageModal'
import { title } from '@/components/primitives'
import { useApiGetStages } from '@/hooks/api/useApi'
import { Alert, Card, Skeleton } from '@heroui/react'
import { TheaterIcon } from 'lucide-react'
import NextLink from 'next/link'

export default function StagesPage() {
  const { data: stages, error, isLoading } = useApiGetStages()

  return (
    <section className='flex flex-col gap-6 py-8'>
      <div className='flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center'>
        <h1 className={title({ size: 'sm' })}>Stages</h1>
        <AddStageModal />
      </div>

      {error && (
        <Alert status='danger'>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Unable to load stages</Alert.Title>
            <Alert.Description>
              Something went wrong while loading stages. Please try again later.
            </Alert.Description>
          </Alert.Content>
        </Alert>
      )}

      {isLoading ? (
        <div className='flex flex-col gap-3'>
          {Array.from({ length: 3 }).map((_, index) => (
            <Skeleton key={index} className='h-16 w-full rounded-lg' />
          ))}
        </div>
      ) : stages && stages.length > 0 ? (
        <div className='grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3'>
          {stages.map(stage => (
            <NextLink key={stage.id} href={`/stages/${stage.id}`}>
              <Card className='h-full transition-shadow hover:shadow-md'>
                <Card.Header className='flex flex-row items-center gap-3'>
                  <TheaterIcon className='text-accent' size={20} />
                  <Card.Title>{stage.name}</Card.Title>
                </Card.Header>
              </Card>
            </NextLink>
          ))}
        </div>
      ) : (
        <p className='text-muted'>No stages yet.</p>
      )}
    </section>
  )
}
