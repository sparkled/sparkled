'use client'

import { Button } from '@heroui/react'
import { Pencil } from 'lucide-react'
import { useRouter } from 'next/navigation'

export type SequenceActionsProps = {
  sequenceId: string
}

export function SequenceActions({ sequenceId }: SequenceActionsProps) {
  const router = useRouter()

  return (
    <Button
      isIconOnly
      aria-label='Edit sequence'
      size='sm'
      variant='ghost'
      onPress={() => router.push(`/sequences/${sequenceId}`)}
    >
      <Pencil size={16} />
    </Button>
  )
}
