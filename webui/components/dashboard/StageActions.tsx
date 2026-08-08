'use client'

import { Button } from '@heroui/react'
import { Pencil } from 'lucide-react'
import { useRouter } from 'next/navigation'

export type StageActionsProps = {
  stageId: string
}

export function StageActions({ stageId }: StageActionsProps) {
  const router = useRouter()

  return (
    <Button
      isIconOnly
      aria-label='Edit stage'
      size='sm'
      variant='ghost'
      onPress={() => router.push(`/stages/${stageId}`)}
    >
      <Pencil size={16} />
    </Button>
  )
}
