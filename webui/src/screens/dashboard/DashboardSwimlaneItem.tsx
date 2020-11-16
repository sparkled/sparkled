import { Popover } from '@material-ui/core'
import React, { ReactNode } from 'react'
import styled from 'styled-components'
import Icons from '../../components/Icons'
import RoundButton from '../../components/RoundButton'

const S = {
  Container: styled.div`
    position: relative;
    border-bottom: 1px solid rgba(0, 0, 0, 0.3);
    padding: 6px 4px 12px 12px;
  `,

  StatusIndicator: styled.div<{ color: string }>`
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 5px;
    background: ${p => p.color};
  `,

  TopRow: styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  `,

  Header: styled.h2`
    font-weight: normal;
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    margin: 0;
    text-overflow: ellipsis;
  `,

  MoreButton: styled.div``,

  Subtitle: styled.h3`
    margin: 0;
    font-weight: normal;
    font-size: 12px;
    color: #ccc;
  `,
}

type Props = {
  className?: string
  status?: 'danger' | 'success'
  title: string
  subtitle?: string
  actions?: ReactNode
}

const DashboardSwimlaneItem: React.FC<Props> = props => {
  const [anchorEl, setAnchorEl] = React.useState<Element | null>(null)
  const handleClick = (event: Event) => setAnchorEl(event?.currentTarget as Element)
  const handleClose = () => setAnchorEl(null)

  const color = props.status === 'danger' ? '#bf4b4b' : 'transparent'
  return (
    <S.Container className={props.className}>
      <S.StatusIndicator color={color} />
      <S.TopRow>
        <S.Header>{props.title}</S.Header>

        {props.actions && (
          <RoundButton size={32} onClick={handleClick}>
            <Icons.More size={16} />
          </RoundButton>
        )}
      </S.TopRow>
      <S.Subtitle>{props.subtitle ?? ''}</S.Subtitle>

      <Popover
        open={!!anchorEl}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'center',
        }}
      >
        {props.actions}
      </Popover>
    </S.Container>
  )
}

export default DashboardSwimlaneItem
