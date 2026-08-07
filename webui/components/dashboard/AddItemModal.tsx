'use client'

import { Button, Modal, useOverlayState } from '@heroui/react'
import { Plus } from 'lucide-react'
import { FormEvent, ReactNode } from 'react'

export type AddItemSubmitHandler = (event: FormEvent<HTMLFormElement>, close: () => void) => void

export type AddItemModalProps = {
  title: string
  ariaLabel: string
  formId: string
  submitLabel: string
  isSubmitting?: boolean
  error?: string | null
  onSubmit: AddItemSubmitHandler
  children: ReactNode
}

export function AddItemModal({
  title,
  ariaLabel,
  formId,
  submitLabel,
  isSubmitting,
  error,
  onSubmit,
  children,
}: AddItemModalProps) {
  const state = useOverlayState()

  return (
    <>
      <Button isIconOnly aria-label={ariaLabel} size='sm' variant='ghost' onPress={state.open}>
        <Plus size={16} />
      </Button>
      <Modal.Backdrop isOpen={state.isOpen} onOpenChange={state.setOpen}>
        <Modal.Container>
          <Modal.Dialog className='sm:max-w-md'>
            <Modal.CloseTrigger />
            <Modal.Header>
              <Modal.Heading>{title}</Modal.Heading>
            </Modal.Header>
            <Modal.Body>
              <form
                className='flex flex-col gap-4'
                id={formId}
                onSubmit={event => onSubmit(event, state.close)}
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
                {isSubmitting ? 'Adding...' : submitLabel}
              </Button>
            </Modal.Footer>
          </Modal.Dialog>
        </Modal.Container>
      </Modal.Backdrop>
    </>
  )
}
