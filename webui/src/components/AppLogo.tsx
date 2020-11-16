import React from 'react'
import { Hidden, Visible } from 'react-grid-system'
import smallLogo from '../images/logo-small.svg'
import largeLogo from '../images/logo-large.svg'

declare interface Props {
  className?: string
}

const AppLogo: React.FC<Props> = (props: Props) => {
  return (
    <>
      <Visible xs sm>
        <img className={props.className} src={smallLogo} alt="Sparkled" />
      </Visible>
      <Hidden xs sm>
        <img className={props.className} src={largeLogo} alt="Sparkled" />
      </Hidden>
    </>
    )
}

export default AppLogo
