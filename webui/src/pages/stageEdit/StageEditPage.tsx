import { IconButton, Theme } from '@material-ui/core'
import { Save, Tune } from '@material-ui/icons'
import { makeStyles, useTheme } from '@material-ui/styles'
import React, { useCallback, useEffect, useReducer } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import ErrorCard from '../../components/ErrorCard'
import PageContainer from '../../components/PageContainer'
import StageEditor from '../../components/stageEditor/StageEditor'
import { loadStage, saveStage } from '../../api/stageApi'
import { StageViewModel } from '../../types/ViewModel'
import { setPageTitle } from '../../utils/pageUtils'
import { useSnackbar } from 'notistack'

const useStyles = makeStyles(() => ({
  // The page container never overflows, and the editor tools need to be hidden when they slide offscreen.
  pageContainer: {
    overflow: 'hidden'
  },
  container: {
    display: 'flex',
    justifyContent: 'center',
    width: '100%',
    height: '100%',
    overflow: 'hidden'
  }
}))

class State {
  public mounted: boolean = false
  public stage: StageViewModel | null = null
  public editedStage: StageViewModel | null = null
  public loading: boolean = false
  public loadError: [string, string] | null = null
  public saving: boolean = false
  public saveError: [string, string] | null = null
  public toolsVisible: boolean = false
}

type Action =
  | { type: 'Load' }
  | { type: 'LoadSuccess'; payload: StageViewModel }
  | { type: 'LoadFailure'; payload: [string, string] | null }
  | { type: 'Update'; payload: StageViewModel }
  | { type: 'Save' }
  | { type: 'SaveSuccess' }
  | { type: 'SaveFailure'; payload: [string, string] | null }
  | { type: 'ToggleTools' }

const reducer: React.Reducer<State, Action> = (state, action): State => {
  switch (action.type) {
    case 'Load':
      return { ...state, mounted: true, loading: true, loadError: null }
    case 'LoadSuccess':
      return { ...state, loading: false, stage: action.payload }
    case 'LoadFailure':
      return { ...state, loading: false, loadError: action.payload }
    case 'Update':
      return { ...state, loading: false, editedStage: action.payload }
    case 'Save':
      return { ...state, mounted: true, saving: true, saveError: null }
    case 'SaveSuccess':
      return { ...state, saving: false }
    case 'SaveFailure':
      return { ...state, saving: false, saveError: action.payload }
    case 'ToggleTools':
      return { ...state, saving: false, toolsVisible: !state.toolsVisible }
    default:
      return state
  }
}

type Props = RouteComponentProps<{ stageId: string | undefined }>

const StageEditPage: React.FC<Props> = props => {
  const theme = useTheme<Theme>()

  const toolsInitiallyVisible = window.outerWidth >= theme.breakpoints.values.sm
  const [state, dispatch] = useReducer(reducer, {
    ...new State(),
    toolsVisible: toolsInitiallyVisible
  })
  const classes = useStyles()
  const snackbar = useSnackbar()

  const toggleTools = useCallback(() => dispatch({ type: 'ToggleTools' }), [])

  useEffect(() => {
    if (!state.mounted) {
      setPageTitle('Edit Stage')
      dispatch({ type: 'Load' })
      loadStage(
        Number(props.match.params.stageId),
        stage => dispatch({ type: 'LoadSuccess', payload: stage }),
        error => dispatch({ type: 'LoadFailure', payload: error })
      )
    }
  })

  const updateStage = (stage: StageViewModel) =>
    dispatch({ type: 'Update', payload: stage })

  let content = <></>
  if (state.loadError !== null) {
    const [title, body] = state.loadError
    content = (
      <ErrorCard
        title={title}
        body={body}
        linkUrl="/stages"
        linkText="Return to stage list"
      />
    )
  } else if (state.stage) {
    const { stage, toolsVisible } = state
    content = (
      <StageEditor
        stage={stage}
        onStageUpdate={updateStage}
        toolsVisible={toolsVisible}
        editable
      />
    )
  }

  const save = () => {
    dispatch({ type: 'Save' })
    saveStage(
      state.editedStage!,
      () => {
        snackbar.enqueueSnackbar('Stage saved')
        dispatch({ type: 'SaveSuccess' })
      },
      error => dispatch({ type: 'SaveFailure', payload: error })
    )
  }

  const pageBody = <div className={classes.container}>{content}</div>
  const actions = (
    <>
      <IconButton
        onClick={save}
        disabled={state.editedStage === null || state.saving}
      >
        <Save />
      </IconButton>
      <IconButton onClick={toggleTools}>
        <Tune />
      </IconButton>
    </>
  )

  return (
    <PageContainer
      className={classes.pageContainer}
      spacing={0}
      actions={actions}
    >
      {pageBody}
    </PageContainer>
  )
}

export default StageEditPage
