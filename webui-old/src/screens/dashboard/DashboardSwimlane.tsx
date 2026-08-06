import React from 'react'
import styled from 'styled-components'
import Icons from '../../components/Icons'
import RoundButton from '../../components/RoundButton'

const S = {
  Container: styled.div<{ area: string }>`
    display: flex;
    flex-direction: column;
    grid-area: ${p => p.area};
    overflow: hidden;
    background: #414141;
    border-radius: 5px;
  `,

  HeaderRow: styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 8px 4px 8px 12px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.3);
    background: rgba(255, 255, 255, .1);
  `,

  Header: styled.h1`
    font-weight: normal;
    font-size: 18px;
    margin: 0;
  `,

  AddButton: styled.div``,

  ItemList: styled.div`
    height: 100%;
    overflow: auto;
  `,
}

type Props = {
  className?: string
  gridArea: string
  title: string
  onAdd?: () => void
}

const DashboardSwimlane: React.FC<Props> = props => {
  return (
    <S.Container area={props.gridArea} className={props.className}>
      <S.HeaderRow>
        <S.Header>{props.title}</S.Header>
        <RoundButton size={32} onClick={() => props.onAdd?.()}>
          <Icons.Plus size={24} />
        </RoundButton>
      </S.HeaderRow>

      <S.ItemList>{props.children}</S.ItemList>
    </S.Container>
  )
}

export default DashboardSwimlane
