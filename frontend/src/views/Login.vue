<template>
  <v-container
    class="fill-height"
    fluid
  >
    <v-row
      align="center"
      justify="center"
    >
      <v-col
        cols="12"
        sm="8"
        md="4"
        lg="4"
      >
        <v-card 
          elevation="0"
        >
          <a href="/">
            <v-img
              src="@/assets/logo.png"
              alt="logo"
              contain
              height="100"
              class="mb-3"
            />
          </a>

          <v-card-text>
            <v-form>
              <v-text-field
                v-model="email"
                prepend-icon="mdi-mail"
                name="email"
                label="Email"
                type="text"
              />
              <v-text-field
                id="password"
                v-model="password"
                prepend-icon="mdi-lock"
                name="password"
                label="Password"
                type="password"
              />

              <v-btn
                class="blue-grey darken-4 mb-2"
                block
                dark
                x-large
                @click="login"
              >
                Login
              </v-btn>
            
              <v-card-actions>
                <v-spacer />

                No Accout? <a
                  class="pl-2"
                  href="/signup"
                >
                  Sign Up
                </a>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

export default {
    data () {
        return {
            email: "",
            password: "",
            redirect: this.$route.query.redirect || undefined
        }
    },
    methods: {
      login() {
        this.$store.dispatch('auth/login', {
          email: this.email,
          password: this.password,
          redirect: this.redirect
        })
      }
    }
}
</script>

<style>

</style>