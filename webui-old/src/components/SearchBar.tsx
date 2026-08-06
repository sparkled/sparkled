import React, { ChangeEventHandler } from 'react'
import styled from 'styled-components'

const S = {
  Input: styled.input`
    border-radius: 4px;
    background: rgba(0, 0, 0, 0.2);
    transition: background .2s;
    padding: 4px 8px;
    min-width: 100px;
    border: 0;
    outline: none;
    font-weight: bold;
    color: #fff;

    &:focus {
      background: rgba(0, 0, 0, 0.3);
    }

    ::placeholder {
      color: #fff;
      opacity: 0.5;
    }
  `,
}

type Props = {
  className?: string
  value?: string
  onChange?: ChangeEventHandler<HTMLInputElement>
}

const SearchBar: React.FC<Props> = props => {
  return (
    <S.Input
      className={props.className}
      type='text'
      placeholder='Search'
      value={props.value}
      onChange={props.onChange}
    />
  )
}

export default SearchBar
