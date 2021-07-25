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
          <router-link to="/">
            <v-img
              src="@/assets/logo.png"
              alt="logo"
              contain
              height="100"
              class="mb-3"
              to="/"
            />
          </router-link>
    
          <p class="text-h4 text-center">
            Create your account
          </p>

          <v-card-text>
            <v-form>
              <v-text-field
                v-model="user.email"
                prepend-icon="mdi-mail"
                name="email"
                label="Email"
                type="text"
                hint="e.g. board@board.kr"
                required
              />
              <v-alert
                v-if="validationError.email"
                type="error"
                text
                dense
              >
                {{ validationError.email }}
              </v-alert>

              <v-text-field 
                v-model="user.name"
                prepend-icon="mdi-account"
                name="name"
                label="Name"
                type="text"
                required
              />
              <v-alert
                v-if="validationError.name"
                type="error"
                text
                dense
              >
                {{ validationError.name }}
              </v-alert>

              <v-text-field
                id="password"
                v-model="user.password"
                prepend-icon="mdi-lock"
                name="password"
                label="Password"
                type="password"
                required
              />
              <v-alert
                v-if="validationError.password"
                type="error"
                text
                dense
              >
                {{ validationError.password }}
              </v-alert>

              <v-text-field
                id="passwordConfirmation"
                v-model="user.passwordConfirmation"
                prepend-icon="mdi-lock"
                name="passwordConfirmation"
                label="Password Confirmation"
                type="password"
              />
              <v-alert
                v-if="validationError.passwordConfirmation"
                type="error"
                text
                dense
              >
                {{ validationError.passwordConfirmation }}
              </v-alert>
              
              <v-text-field
                id="phoneNumber"
                v-model="user.phoneNumber"
                prepend-icon="mdi-phone"
                name="phoneNumber"
                label="Phone Number"
                type="text"
                hint="e.g. 01012345678"
              />
              <v-alert
                v-if="validationError.phoneNumber"
                type="error"
                text
                dense
              >
                {{ validationError.phoneNumber }}
              </v-alert>

              <v-btn
                class="blue-grey darken-4 mb-2"
                block
                dark
                x-large
                @click="signup"
              >
                SIGNUP
              </v-btn>
            
              <v-card-actions>
                <v-spacer />

                <p>
                  Aready a member? <a href="/login">
                    Log in
                  </a>
                </p>
              </v-card-actions>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
    data: () => ({
        user: {
            email: "",
            name: "",
            password: "",
            passwordConfirmation: "",
            phoneNumber: "",
        }
    }),
    computed: {
        ...mapState('error', {
             validationError: state => state.validationError
        })
    },
    methods: {
        signup() {
            this.$store.dispatch('auth/signup', this.user)
        }
    }
}
</script>

<style>

</style>