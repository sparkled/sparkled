import { useCallback, useState } from 'react'
import { PlaylistViewModel } from '../apiTypes'
import useAxios from '../useAxios'

function useApiCreatePlaylist() {
  const axios = useAxios()

  const [submitting, setSubmitting] = useState(false)

  const createPlaylist = useCallback(
    async (playlist: PlaylistViewModel) => {
      setSubmitting(true)

      try {
        return await axios.post('/playlists', playlist)
      } catch (e) {
      } finally {
        setSubmitting(false)
      }
    },
    [axios],
  )

  return { createPlaylist, submitting }
}

export default useApiCreatePlaylist
