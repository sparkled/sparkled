import React, { FormEventHandler, PropsWithChildren, ReactElement, useCallback } from 'react'
import styled from 'styled-components'
import StyledModal from 'styled-react-modal'
import theme from '../config/theme'
import { ModalManager } from '../hooks/useModal'
import Icons from './Icons'

const S = {
  Modal: StyledModal.styled`
    background: ${theme.colors.greyMedium};
    border-radius: 16px;
    width: 300px;
    overflow: hidden;
    box-shadow: rgba(0, 0, 0, 0.25) 0px 13px 27px -5px, rgba(0, 0, 0, 0.3) 0px 8px 16px -8px;
  `,

  Fieldset: styled.fieldset`
    border: none;
    padding: 0;
    margin: 0;
  `,

  ErrorContainer: styled.div`
    background: ${theme.colors.redDark};
    border-radius: 6px;
    padding: 8px 24px;
    margin: 32px -24px -24px -24px;
  `,

  ModalHeader: styled.div`
    display: flex;
    justify-content: space-between;
    padding: 32px;
  `,

  ModalTitle: styled.h1`
    font-size: 22px;
    font-weight: 500;
    margin: 0;
    font-family: Cabin, sans-serif;
  `,

  CloseButton: styled.a<{ enabled: boolean }>`
    pointer-events: ${p => p.enabled ? 'all' : 'none'};
    visibility: ${p => p.enabled ? 'visible' : 'hidden'};
  `,

  CloseIcon: styled(Icons.Close)`
    width: 32px;
    height: 32px;
    padding: 8px;
    background: transparent;
    border-radius: 50%;
    transition: background-color .2s;
    margin-right: -8px;

    &:hover {
      background: #333;
    }
  `,

  ModalBody: styled.div`
    padding: 0 32px 32px 32px;
  `,

  ModalFooter: styled.div`
    display: flex;
    justify-content: flex-end;
    border-top: 1px solid ${theme.colors.greyDark};
    background: ${theme.colors.greyExtraDark};
    padding: 16px 32px;
  `
}

type Props = {
  modal: ModalManager
  title: string
  buttons: ReactElement
  onSubmit?: FormEventHandler<any>
  disabled?: boolean
}

const Modal: React.FC<PropsWithChildren<Props>> = props => {
  const submit = useCallback(async (event: React.SyntheticEvent<HTMLFormElement>) => {
    props.onSubmit?.(event)
  }, [props])

  const hideIfNotLoading = useCallback(() => {
    if (!props.modal.isLoading) {
      props.modal.hide()
    }
  }, [props.modal])

  return (
    <S.Modal isOpen={props.modal.isOpen} onBackgroundClick={hideIfNotLoading}>
      <form onSubmit={submit}>
        <S.Fieldset disabled={props.disabled}>
          <S.ModalHeader>
            <S.ModalTitle>{props.title}</S.ModalTitle>
            <S.CloseButton onClick={props.modal.hide} enabled={props.modal.state.status !== 'loading'}>
              <S.CloseIcon />
            </S.CloseButton>
          </S.ModalHeader>
          <S.ModalBody>
            {props.children}
            {props.modal.state.error && <S.ErrorContainer>{props.modal.state.error}</S.ErrorContainer>}
          </S.ModalBody>
          <S.ModalFooter>
            {props.buttons}
          </S.ModalFooter>
        </S.Fieldset>
      </form>
    </S.Modal>
  )
}

export default Modal

