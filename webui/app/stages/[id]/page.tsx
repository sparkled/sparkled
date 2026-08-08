import { StageEditor } from '@/components/stageEditor/StageEditor'

type StagePageProps = {
  params: Promise<{ id: string }>
}

export default async function StagePage({ params }: StagePageProps) {
  const { id } = await params

  return <StageEditor stageId={id} />
}
