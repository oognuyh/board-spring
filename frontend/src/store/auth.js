import * as authApi from '@/api/auth'
import router from '../router'

export default {
  namespaced: true,
  state: {
    token: null,
    userDetails: null,
    username: null
  },
  mutations: {
    setToken(state, token) {
      state.token = token
    },
    setUserDetails(state, userDetails) {
      state.userDetails = userDetails
      state.username = userDetails === null ? null : userDetails.name
    }
  },
  actions: {
    async login(context, { email, password, redirect }) {
      try {
        const response = await authApi.login(email, password)

        if (response.status === 200) {
          localStorage.setItem('auth-token', response.headers['x-auth-token'])
          localStorage.setItem('refresh-token', response.headers['x-refresh-token'])

          context.dispatch('getUserDetails')

          router.replace(redirect || "/")
        }
      } catch (error) {
        console.log(error)
      }
    },

    async refreshToken(context) {
      try {
        const refreshToken = localStorage.getItem('refresh-token')
        if (refreshToken) {
          const response = await authApi.refreshToken(refreshToken)

          if (response.status === 200) {
            localStorage.setItem('auth-token', response.headers['x-auth-token'])
            localStorage.setItem('refresh-token', response.headers['x-refresh-token'])
          }
        }
      } catch (error) {
        console.log(error)
      }
    },

    logout(context) {
      context.commit('setUserDetails', null)
      localStorage.removeItem('auth-token')
      localStorage.removeItem('refresh-token')
      router.replace('/login')
    },

    async signup(context, user) {
      try {
        const response = await authApi.signup(user)

        if (response.status === 201) {
          router.replace("/login")
        }
      } catch (error) {
        console.log(error)
      }
    },

    async getUserDetails(context) {
      try {
        if (localStorage.getItem('auth-token') !== null) {
          const response = await authApi.getUserDetails()

          if (response.status === 200) {
            context.commit('setUserDetails', response.data)
          }
        }
      } catch (error) {
        console.log(error)
      }
    },
    async update(context, user) {
      try {
        const response = await authApi.update(user)

        if (response.status === 200) {
          authApi.getUserDetails()
          return true
        }
      } catch (error) {
        console.log(error)
        return false
      }
    }
  }
}
