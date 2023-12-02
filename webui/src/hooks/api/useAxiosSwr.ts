import { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios'
import { useEffect, useRef } from 'react'
import useSWR, { SWRConfiguration, SWRResponse } from 'swr'
import { ErrorViewModel } from '../../types/viewModels.ts'
import useAxios from './useAxios.ts'

export type GetRequest = AxiosRequestConfig | null

export interface AxiosSwr<Data>
  extends Pick<SWRResponse<AxiosResponse<Data>, AxiosError<ErrorViewModel>>, 'isValidating' | 'error' | 'mutate'> {
  data: Data | undefined
  loading: boolean
  response: AxiosResponse<Data> | undefined
}

export interface Config<Data = unknown, Error = unknown>
  extends Omit<SWRConfiguration<AxiosResponse<Data>, AxiosError<Error>>, 'fallbackData'> {
  fallbackData?: Data
}

/**
 * Based on https://github.com/vercel/swr/blob/main/examples/axios-typescript/libs/useRequest.ts
 */
export default function useAxiosSwr<Data = unknown>(
  request: GetRequest,
  isReady: boolean = true,
  { fallbackData, ...config }: Config<Data, ErrorViewModel> = {}
): AxiosSwr<Data> {
  const unloading = useRef(false)

  // The axios instance will have request interceptors for authenticated user screens, and no such
  // interceptors for participant screens.
  const axios = useAxios()

  useEffect(() => {
    const listener = () => (unloading.current = true)
    window.addEventListener('beforeunload', listener)
    return () => window.removeEventListener('beforeunload', listener)
  }, [])

  const { data: response, error, isValidating, mutate } = useSWR<AxiosResponse<Data>, AxiosError<ErrorViewModel>>(
    request && JSON.stringify(request),
    /**
     * NOTE: Typescript thinks `request` can be `null` here, but the fetcher
     * function is actually only called by `useSWR` when it isn't.
     */
    isReady ? () => axios.request<Data>(request!) : null,
    {
      ...config,
      fallbackData: fallbackData && {
        status: 200,
        statusText: 'InitialData',
        config: request!,
        headers: {},
        data: fallbackData,
      },
    }
  )

  const actualError = getActualError(error, unloading.current)

  return {
    data: response?.data,
    response,
    error: actualError,
    isValidating,
    loading: response?.data == null && isValidating,
    mutate,
  }
}

function getActualError(error: AxiosError<ErrorViewModel> | undefined, unloading: boolean) {
  if (unloading && error?.code === 'ECONNABORTED') {
    // Page is closing/refreshing, no need to show error.
    return undefined
  } else {
    return error
  }
}

export const immutableSwr: SWRConfiguration = {
  revalidateIfStale: false,
  revalidateOnFocus: false,
  revalidateOnReconnect: false,
}
