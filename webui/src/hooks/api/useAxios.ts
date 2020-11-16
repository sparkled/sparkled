import Axios from 'axios'
import {useMemo} from 'react'
import { RequestStatus } from './apiTypes'

const baseUrl = process.env.NODE_ENV === 'production' ? '/api' : 'http://localhost:8080/api'

const createAxiosInstance = () => {
  return Axios.create({
    baseURL: baseUrl,
    timeout: 60000,
    headers: {
      Accept: 'application/json'
    }
  })
}

function useAxios() {
  return useMemo(() => {
    console.info(`API URL: ${baseUrl}.`)
    return createAxiosInstance()
  }, [])
}

function isLoadingOrReloading(status?: RequestStatus) {
  return status === 'loading' || status === 'reloading'
}

export { isLoadingOrReloading }
export default useAxios

