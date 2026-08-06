import { title } from '@/components/primitives'
import NextLink from 'next/link'

export default function SequencesPage() {
  return (
    <section className='flex flex-col items-start gap-4 py-8'>
      <h1 className={title({ size: 'sm' })}>Sequences</h1>
      <p className='text-muted'>This page is coming soon.</p>
      <NextLink className='text-accent hover:underline' href='/'>
        Back to dashboard
      </NextLink>
    </section>
  )
}
