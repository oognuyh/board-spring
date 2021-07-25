import * as commentApi from '@/api/comment'
import { save } from '../api/comment'

export default {
  namespaced: true,
  state: {
    comments: []
  },
  mutations: {
    setComments(state, comments) {
      state.comments = comments
    }
  },
  actions: {
    async getCommentsByPostId(context, postId) {
      try {
        const response = await commentApi.getCommentsByPostId(postId)

        if (response.status === 200)
          context.commit('setComments', response.data)
      } catch (error) {
        console.log(error)
      }
    },
    async save(context, post) {
      try {
        const response = await commentApi.save(post)

        if (response.status === 201)
          return true
      } catch (error) {
        console.log(error)
        return false
      }
    },
    async update (context, post) {
      try {
        const response = await commentApi.update(post)

        if (response.status === 200)
          return true
      } catch (error) {
        console.log(error)
        return false
      }
    },
    async remove(context, id) {
      try {
        const response = await commentApi.remove(id)

        if (response.status === 200)
          return true
      } catch (error) {
        console.log(error)
        return false
      }
    }
  }
}
