import * as boardApi from '@/api/board'
import router from '../router'

export default {
  namespaced: true,
  state: {
    currentPage: {},
    post: {
      title: "",
      content: "",
      userId: null,
      author: ""
    }
  },
  mutations: {
    setPost(state, post) {
      state.post = post
    },
    setCurrentPage(state, currentPage) {
      state.currentPage = currentPage
    }
  },
  actions: {
    async getPosts(context, page, size = 10) {
      try {
        const response = await boardApi.getPosts(page, size)

        if (response.status === 200) {
          context.commit('setCurrentPage', response.data)
        }
      } catch (error) {
        console.log(error)
      }
    },
    async getPostById(context, id) {
      try {
        const response = await boardApi.getPostById(id)

        if (response.status === 200) {
          context.commit('setPost', response.data)
        }
      } catch (error) {
        console.log(error)
      }
    },
    async save(context, post) {
      try {
        const response = await boardApi.save(post)

        if (response.status === 200)
          return true
      } catch (error) {
        return false
      }
    },
    async update(context, post) {
      try {
        const response = await boardApi.update(post)

        if (response.status === 200)
          return true
      } catch (error) {
        return false
      }
    },
    async remove(context, id) {
      try {
        const response = await boardApi.remove(id)

        if (response.status === 200)
          return true
      } catch (error) {
        console.log(error)
      }
    }
  }
}
