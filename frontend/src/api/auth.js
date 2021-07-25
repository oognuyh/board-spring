import http from '@/api/http'

export function login(email, password) {
  return http.post('/login', {
    email,
    password
  });
}

export function signup(user) {
  return http.post('/signup', {
    ...user
  })
}

export function getUserDetails() {
  return http.post('/api/v1/user')
}

export function refreshToken(refreshToken) {
  return http.post('/login', {
    refreshToken
  })
}

export function update(user) {
  return http.put('/api/v1/user', {
    ...user
  })
}
