import { ButtonProps } from '@material-ui/core/Button'
import React, { PropsWithChildren } from 'react'
import styled from 'styled-components'
import theme from '../../config/theme'
import LoadingIndicator from './LoadingIndicator'

const BaseButton = styled.button`
  position: relative;
  font-family: Raleway, sans-serif;
  font-weight: 600;
  font-size: 14px;
  display: block;
  background: ${theme.colors.purple};
  border: 0;
  border-radius: 8px;
  color: ${theme.colors.white};
  padding: 8px 12px;
`

const LoadingContainer = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`

const VisibleToggle = styled.span<{ visible: boolean }>`
  visibility: ${p => p.visible ? 'visible' : 'hidden'};
`

const Button: React.FC<PropsWithChildren<ButtonProps & { loading?: boolean }>> = props => {
  return (
    <BaseButton {...props}>
      <VisibleToggle visible={!props.loading}>{props.children}</VisibleToggle>
      <LoadingContainer>
        <LoadingIndicator visible={props.loading ?? false} />
      </LoadingContainer>
    </BaseButton>
  )
}

const Form = {
  Button,
  Label: styled.label`
    font-family: Raleway, sans-serif;
    font-weight: 600;
    font-size: 14px;
    display: block;
    margin-bottom: 4px;
    color: ${theme.colors.greyLight};
  `,

  SubmitButton: styled(Button)``,

  Input: styled.input`
    border-radius: 8px;
    border: 0;
    background: ${theme.colors.greyDark};
    width: 100%;
    font-size: 14px;
    padding: 8px;
    color: ${theme.colors.white};
    outline: 2px solid transparent;
    transition: outline-color .2s;
    font-family: Raleway, sans-serif;

    &:focus {
      outline-color: ${theme.colors.white15};
    }
  `
}

export default Form
