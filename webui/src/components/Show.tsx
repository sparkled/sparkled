import React, { PropsWithChildren } from 'react'

type Props = PropsWithChildren<{
  if: boolean
}>

const Show: React.FC<Props> = props => {
  if (props.if) {
    return <>{props.children}</>
  } else {
    return null
  }
}

export default Show
