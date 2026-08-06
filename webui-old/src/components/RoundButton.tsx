import React, { EventHandler } from 'react'
import styled from 'styled-components'

const S = {
  Container: styled.button<{ size: number }>`
    display: flex;
    align-items: center;
    justify-content: center;
    width: ${p => p.size}px;
    height: ${p => p.size}px;
    border: 0;
    outline: none;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0);
    transition: background .2s;

    &:hover {
      cursor: pointer;
      background: rgba(0, 0, 0, 0.3);
    }
  `,
}

type Props = {
  size: number
  onClick?: EventHandler<any>
}

const RoundButton: React.FC<Props> = props => {
  return (
    <S.Container size={props.size} onClick={props.onClick}>
      {props.children}
    </S.Container>
  )
}

export default RoundButton
