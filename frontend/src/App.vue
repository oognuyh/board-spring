<template>
  <v-app>
    <v-app-bar
      class="blue-grey darken-4"
      dark
      app
    >
      <v-app-bar-nav-icon @click="drawer = !drawer" />

      <v-spacer />

      <template
        v-if="username !== null"
      >
        <span class="mr-2">Hello, {{ username }}</span>
        <v-btn
          icon
          @click="logout"
        >
          <v-icon>
            mdi-logout
          </v-icon>
        </v-btn>
      </template>
      <v-btn
        v-else
        icon
        href="/login"
      >
        <v-icon>
          mdi-login
        </v-icon>
      </v-btn>
      <v-btn
        icon
        href="/my-page"
      >
        <v-icon>mdi-account</v-icon>
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      class="blue-grey darken-4"
      dark
      app
    >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title
            class="pb-3"
          >
            <v-list-item-icon>
              <a href="/">
                <v-img
                  src="@/assets/logo.png"
                  alt="logo"
                  height="24"
                  width="24"
                />
              </a>
            </v-list-item-icon>
            <span class="text-h5">Board</span>
          </v-list-item-title>
          <v-list-item-subtitle class="blue-grey--text text--lighten-5">
            Using Spring Boot and Vue.js
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider />

      <v-list
        dense
        nav
      >
        <v-list-item
          v-for="item in items"
          :key="item.title"
          link
          :to="item.to"
          class="py-1"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <router-view />
    </v-main>

    <v-footer app />
  </v-app>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data: () => ({
    items: [
        { title: 'Board', icon: 'mdi-view-dashboard', to: '/' },
        { title: 'New Post', icon: 'mdi-pencil', to: '/new-post' },
    ],
    drawer: false
  }),
  computed: {
    ...mapState('auth', {
      username: state => state.username
    })
  },
  methods: {
    logout() {
      this.$store.dispatch('auth/logout')
      this.$router.go(this.$router.currentRoute)
    }
  }
}
</script>

<style>

</style>
