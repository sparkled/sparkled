import classNames from 'classnames'
import React, { HTMLAttributes } from 'react'
import { Button, Modal, ModalHeaderProps } from 'react-bootstrap'
import { ModalProps } from 'react-bootstrap/Modal'
import { ReactComponent as CloseIcon } from '../images/close.svg'
import iconStyles from '../styles/icons.module.scss'
import styles from './SparkledModal.module.scss'

type Props = HTMLAttributes<HTMLDivElement> &
  ModalProps & {
    borderless?: boolean
    inline?: boolean
  }


type SparkledModalType = React.FC<Props> & {
  Body: React.FC<React.HTMLAttributes<HTMLDivElement>>
  CloseButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>>
  Footer: React.FC<React.HTMLAttributes<HTMLDivElement>>
  Header: React.FC<React.HTMLAttributes<HTMLDivElement>>
  Title: React.FC<React.HTMLAttributes<HTMLDivElement>>
}

/**
 * A wrapper around a Bootstrap modal that allows the modal to be optionally rendered inline by
 * setting the "inline" prop to true. Also provides some Sparkled-specific styling.
 */
const SparkledModal: SparkledModalType = props => {
  if (props.inline) {
    return (
      <div
        className={classNames(
          props.className,
          styles.inline,
          props.borderless && styles.borderless,
          'modal',
        )}
      >
        <div className='modal-dialog'>
          <div className='modal-content'>{props.children}</div>
        </div>
      </div>
    )
  } else {
    return (
      <Modal
        {...props}
        className={classNames(
          props.className,
          styles.notInline,
          props.borderless && styles.borderless,
        )}
      >
        {props.children}
      </Modal>
    )
  }
}

const CloseButton: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = props => {
  return (
    <Button variant='' {...props} className={classNames(props.className, styles.closeButton)}>
      <CloseIcon className={iconStyles.icon24} />
    </Button>
  )
}

const Title: React.FC<HTMLAttributes<HTMLDivElement>> = props => {
  return (
    <Modal.Title
      {...props}
      className={classNames(props.className, styles.modalTitle, 'heading3')}
      as='b'
    >
      {props.children}
    </Modal.Title>
  )
}

const Footer: React.FC<HTMLAttributes<HTMLDivElement>> = props => {
  return (
    <Modal.Footer {...props} className={classNames(props.className, 'border-top-0')} as='b'>
      {props.children}
    </Modal.Footer>
  )
}

const Header: React.FC<ModalHeaderProps> = props => {
  return (
    <Modal.Header {...props} className={classNames(props.className, styles.modalHeader)}>
      {props.children}
    </Modal.Header>
  )
}

SparkledModal.Body = Modal.Body
SparkledModal.CloseButton = CloseButton
SparkledModal.Footer = Footer
SparkledModal.Header = Header
SparkledModal.Title = Title

export default SparkledModal
