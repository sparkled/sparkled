import { SequenceEditor } from '@/components/sequenceEditor/SequenceEditor'

type SequencePageProps = {
  params: Promise<{ id: string }>
}

export default async function SequencePage({ params }: SequencePageProps) {
  const { id } = await params

  return <SequenceEditor sequenceId={id} />
}
