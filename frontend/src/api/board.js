import http from '@/api/http'

export function getPosts(page, size) {
  return http.get('/api/v1/post', {
    params: {
      page: page,
      size: size
    }
  })
}

export function getPostById(id) {
  return http.get('/api/v1/post/' + id)
}

export function save(post) {
  return http.post('/api/v1/post', {...post})
}

export function update(post) {
  return http.put('/api/v1/post', {
    ...post
  })
}

export function remove(id) {
  return http.delete('/api/v1/post/' + id)
}
