import Vue from 'vue'
import Vuex from 'vuex'
import auth from './auth'
import error from './error'
import board from './board'
import comment from './comment'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    auth,
    error,
    board,
    comment
  }
})
