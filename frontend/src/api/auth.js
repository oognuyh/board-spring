import http from '@/api/http'

export function login(email, password) {
  return http.post('/api/v1/login', {
    email,
    password
  });
}

export function signup(user) {
  return http.post('/api/v1/signup', {
    ...user
  })
}

export function getUserDetails() {
  return http.post('/api/v1/user')
}

export function refreshToken(refreshToken) {
  return http.post('/api/v1/login', {
    refreshToken
  })
}

export function update(user) {
  return http.put('/api/v1/user', {
    ...user
  })
}
