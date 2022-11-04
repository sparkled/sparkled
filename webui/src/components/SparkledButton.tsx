import classNames from 'classnames'
import React from 'react'
import { Button, ButtonProps, Spinner } from 'react-bootstrap'
import Show from './Show'
import styles from './SparkledButton.module.scss'

type Props = ButtonProps & {
  submitting?: boolean
}

const SparkledButton: React.FC<Props> = props => {
  const content = !props.submitting ? (
    <>{props.children}</>
  ) : (
    <div className='invisible'>{props.children}</div>
  )

  const buttonProps = { ...props }
  delete buttonProps.submitting

  return (
    <Button
      {...buttonProps}
      disabled={props.disabled || props.submitting}
      className={classNames(props.className, 'position-relative')}
    >
      {content}
      <Show if={props.submitting === true}>
        <div className={classNames(props.submitting && styles.submitting)}>
          <Spinner animation='border' size='sm' variant='light' />
        </div>
      </Show>
    </Button>
  )
}

export default SparkledButton
