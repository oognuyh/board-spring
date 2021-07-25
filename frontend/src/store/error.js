export default {
  namespaced: true,
  state: {
    validationError: {}
  },
  mutations: {
    setValidationError(state, error) {
      state.validationError = error
    }
  }
}
