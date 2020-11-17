import React from 'react'
import styled from 'styled-components'
import { RequestStatus } from '../hooks/api/apiTypes'
import { isLoadingOrReloading } from '../hooks/api/useAxios'

type Props = {
  className?: string
  loading: RequestStatus | boolean
}

const S = {
  ContentContainer: styled.div`
    display: flex;
    flex: 1;
  `,

  SpinnerContainer: styled.div`
    display: flex;
    width: 100%;
    height: 100%;
    align-items: center;
    justify-content: center;
  `,
}

const PageLoadingContainer: React.FC<Props> = props => {
  const loading = props.loading === true || isLoadingOrReloading(props.loading as RequestStatus)
  return loading ? (
    <Spinner size={128} />
  ) : (
    <S.ContentContainer className={props.className}>{props.children}</S.ContentContainer>
  )
}

const Spinner: React.FC<{ size?: number }> = props => {
  return (
    <S.SpinnerContainer>
      <svg viewBox='0 0 38 38' stroke='currentColor' width={props.size ?? 50} role='progressbar'>
        <g transform='translate(1 1)' strokeWidth='2' fill='none' fillRule='evenodd'>
          <circle strokeOpacity='0.5' cx='18' cy='18' r='18' />
          <path d='M36 18c0-9.94-8.06-18-18-18'>
            <animateTransform
              attributeName='transform'
              type='rotate'
              from='0 18 18'
              to='360 18 18'
              dur='1s'
              repeatCount='indefinite'
            />
          </path>
        </g>
      </svg>
    </S.SpinnerContainer>
  )
}

export default PageLoadingContainer
