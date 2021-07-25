import Vue from 'vue'
import VueRouter from 'vue-router'
import NewPost from '@/views/NewPost'
import Post from '@/views/Post'
import Board from '@/views/Board'
import MyPage from '@/views/MyPage'
import Login from '@/views/Login'
import Signup from '@/views/Signup'
import NotFound from '@/views/NotFound'

Vue.use(VueRouter)

const requireAuth = () => (to, from, next) => {
  if (localStorage.getItem('auth-token') !== null) {
      return next()
  }

  next('/login?redirect=' + to.path)
}

const routes = [
    {
        path: '/',
        name: 'Board',
        component: Board
    },
    {
      path: '/new-post',
      name: 'NewPost',
      component: NewPost,
      beforeEnter: requireAuth()
    },
    {
      path: '/post/:id',
      name: 'Post',
      component: Post
    },

    {
        path: '/my-page',
        name: 'MyPage',
        component: MyPage,
        beforeEnter: requireAuth()
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/signup',
      name: 'Signup',
      component: Signup
    },

    {
      path: '*',
      name: 'NotFound',
      component: NotFound
    }
]

export default new VueRouter({
    mode: 'history',
    routes
})

