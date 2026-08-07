'use client'

import { Button, Modal } from '@heroui/react'
import { FormEvent, ReactNode } from 'react'

export type EditItemSubmitHandler = (event: FormEvent<HTMLFormElement>, close: () => void) => void

export type EditItemModalProps = {
  title: string
  formId: string
  submitLabel?: string
  isSubmitting?: boolean
  error?: string | null
  dialogClassName?: string
  isOpen: boolean
  onOpenChange: (isOpen: boolean) => void
  onSubmit: EditItemSubmitHandler
  children: ReactNode
}

export function EditItemModal({
  title,
  formId,
  submitLabel = 'Save changes',
  isSubmitting,
  error,
  dialogClassName = 'sm:max-w-md',
  isOpen,
  onOpenChange,
  onSubmit,
  children,
}: EditItemModalProps) {
  return (
    <Modal.Backdrop isOpen={isOpen} onOpenChange={onOpenChange}>
      <Modal.Container>
        <Modal.Dialog className={dialogClassName}>
          <Modal.CloseTrigger />
          <Modal.Header>
            <Modal.Heading>{title}</Modal.Heading>
          </Modal.Header>
          <Modal.Body>
            <form
              className='flex flex-col gap-4'
              id={formId}
              onSubmit={event => onSubmit(event, () => onOpenChange(false))}
            >
              {children}
            </form>
            {error && <p className='text-danger mt-3 text-sm'>{error}</p>}
          </Modal.Body>
          <Modal.Footer>
            <Button slot='close' variant='secondary'>
              Cancel
            </Button>
            <Button form={formId} isDisabled={isSubmitting} type='submit'>
              {isSubmitting ? 'Saving...' : submitLabel}
            </Button>
          </Modal.Footer>
        </Modal.Dialog>
      </Modal.Container>
    </Modal.Backdrop>
  )
}
