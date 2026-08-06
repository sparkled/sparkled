import { StagePropViewModel } from './viewModels.ts'

export class StageViewModel {
  public id: string | null = null
  public name: string = ''
  public width: number = 0
  public height: number = 0
  public stageProps: StagePropViewModel[] = []
}
