import axios from 'axios'
import store from '@/store'
import router from '../router'

const instance =  axios.create({
  baseURL: 'http://localhost:9999',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

instance.interceptors.request.use(function(config) {
  const authToken = localStorage.getItem('auth-token')
  if (authToken) {
    config['headers'] = {
      Authorization: authToken
    }
  }

  return config
})

instance.interceptors.response.use(
  function (response) {
    store.commit('error/setValidationError', {})

    return response
  },
  async function (error) {
    if (error.response.status === 400) {
      store.commit('error/setValidationError', error.response.data)
    } else if (error.response.status === 401) {
      const originalRequest = error.config
      if (!originalRequest._retry) {
        originalRequest._retry = true

        await store.dispatch("auth/refreshToken")

        originalRequest.headers.Authorization = localStorage.getItem('auth-token')

        return axios(originalRequest)
      }

    } else if (error.response.status === 403) {
      if (localStorage.getItem('auth-token') === null) {
        router.push("/login")
      }
    } else if (error.response.status === 404) {
      router.replace("/404")
    }
    return Promise.reject(error)
  }
)

export default instance
