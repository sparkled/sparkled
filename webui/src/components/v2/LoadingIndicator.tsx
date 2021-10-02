import React from 'react'
import { ThreeBounce } from 'styled-spinkit'
import theme from '../../config/theme'

type Props = {
  visible: boolean
  size?: number
}

const LoadingIndicator: React.FC<Props> = props => {
  return !props.visible ? null : (
    <ThreeBounce color={theme.colors.white} size={props.size ?? 32} />
  )
}

export default LoadingIndicator

