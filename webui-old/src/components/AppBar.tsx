import { Popover } from '@material-ui/core'
import React from 'react'
import { Col, Container, Row } from 'react-grid-system'
import { useDispatch } from 'react-redux'
import styled from 'styled-components'
import { setQuery } from '../store/reducers/dashboardScreenReducer'
import { useAppSelector } from '../store/store'
import AppLogo from './AppLogo'
import BrightnessToggle from './BrightnessToggle'
import Icons from './Icons'
import RoundButton from './RoundButton'
import SearchBar from './SearchBar'

const S = {
  Nav: styled.nav`
    display: flex;
    flex-direction: row;
    align-items: center;
    background: #2596f1;
    height: 50px;
  `,

  Container: styled(Container)`
    flex: 1;
  `,

  LogoContainer: styled.div`
    height: 28px;
    align-self: flex-start;
  `,

  Logo: styled(AppLogo)`
    height: 100%;
  `,

  SearchBar: styled(SearchBar)`
    flex: 1;
    height: 28px;
    justify-self: center;
  `,

  MiddleCol: styled(Col)`
    display: flex;
  `,

  RightCol: styled(Col)`
    display: flex;
    justify-content: flex-end;
  `
}

const AppBar = () => {
  const [anchorEl, setAnchorEl] = React.useState<Element | null>(null)
  const handleClick = (event: Event) => setAnchorEl(event?.currentTarget as Element)
  const handleClose = () => setAnchorEl(null)

  const dispatch = useDispatch()
  const { query } = useAppSelector(state => state.dashboardScreen)

  return (
    <S.Nav>
      <S.Container>
        <Row align='center'>
          <Col xs={2} sm={4}>
            <S.LogoContainer>
              <S.Logo />
            </S.LogoContainer>
          </Col>

          <S.MiddleCol xs={7} sm={4}>
            <S.SearchBar value={query} onChange={e => dispatch(setQuery(e.target.value))} />
          </S.MiddleCol>

          <S.RightCol xs={3} sm={4}>
            <RoundButton size={42} onClick={handleClick}>
              <Icons.Slider size={20} />
            </RoundButton>
          </S.RightCol>
        </Row>
      </S.Container>

      <Popover
        open={!!anchorEl}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center'
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'center'
        }}
      >
        <div style={{ width: 300, padding: 16 }}>
          <div>Brightness</div>
          <BrightnessToggle />
        </div>
      </Popover>
    </S.Nav>
  )
}

export default AppBar
