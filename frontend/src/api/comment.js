import http from '@/api/http'

export function getCommentsByPostId (postId) {
  return http.get('/api/v1/comment', {
    params: {
      postId: postId
    }
  })
}

export function save(comment) {
  return http.post('/api/v1/comment', {
    ...comment
  })
}

export function update(comment) {
  return http.put('/api/v1/comment', {
    ...comment
  })
}

export function remove(id) {
  return http.delete('/api/v1/comment/' + id)
}
