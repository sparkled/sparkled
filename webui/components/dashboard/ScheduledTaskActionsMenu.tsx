'use client'

import { EditScheduledTaskModal } from '@/components/dashboard/EditScheduledTaskModal'
import { Button, Dropdown, useOverlayState } from '@heroui/react'
import { EllipsisVertical, Pencil } from 'lucide-react'
import { Key } from 'react'

export type ScheduledTaskActionsMenuProps = {
  taskId: string
}

export function ScheduledTaskActionsMenu({ taskId }: ScheduledTaskActionsMenuProps) {
  const editState = useOverlayState()

  const handleAction = (key: Key) => {
    if (key === 'edit') {
      editState.open()
    }
  }

  return (
    <>
      <Dropdown>
        <Dropdown.Trigger>
          <Button isIconOnly aria-label='More actions' size='sm' variant='ghost'>
            <EllipsisVertical size={16} />
          </Button>
        </Dropdown.Trigger>
        <Dropdown.Popover placement='bottom end'>
          <Dropdown.Menu onAction={handleAction}>
            <Dropdown.Item id='edit' textValue='Edit'>
              <Pencil size={16} />
              Edit
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown.Popover>
      </Dropdown>
      <EditScheduledTaskModal
        isOpen={editState.isOpen}
        taskId={taskId}
        onOpenChange={editState.setOpen}
      />
    </>
  )
}
