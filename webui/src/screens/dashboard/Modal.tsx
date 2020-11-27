import ReactModal from 'react-modal'
import React, { ReactNode } from 'react'
import styled from 'styled-components'

const S = {
  Modal: styled(ReactModal)`
    min-width: 300px;
    border-radius: 4px;
    outline: none;
    background: #414141;
    overflow: hidden;
  `,

  Header: styled.div`
    padding: 16px;
    border-bottom: 1px solid rgba(0, 0, 0, .3);
    background: #545454;
  `,

  Title: styled.h1`
    margin: 0;
    font-size: 16px;
  `,
}

type Props = {
  isOpen: boolean
  title: string
  buttons: ReactNode
  className?: string
}

const modalStyles = {
  overlay: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: 'rgba(0, 0, 0, .6)'
  },
}

const Modal: React.FC<Props> = props => {
  return (
    <S.Modal isOpen={props.isOpen} style={modalStyles}>
      <S.Header>
        <S.Title>{props.title}</S.Title>
      </S.Header>
      {props.children}
      {props.buttons}
    </S.Modal>
  )
}

export default Modal
