<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card
          elevation="0"
        >
          <v-card-title>
            My page
          </v-card-title>

          <v-card-text>
            <v-form>
              <v-text-field
                v-model="userDetails.email"
                prepend-icon="mdi-mail"
                name="email"
                label="Email"
                disabled
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
                v-model="userDetails.name"
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
                v-model="password"
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
                v-model="passwordConfirmation"
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
                v-model="userDetails.phoneNumber"
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
            </v-form>
          </v-card-text>

          <v-card-actions>
            <v-btn
              class="blue-grey darken-4"
              block
              dark
              @click="updateUser"
            >
              update
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
    data () {
        return {
            password: "",
            passwordConfirmation: "",
        }
    },
    computed: {
        ...mapState({
            validationError: state => state.error.validationError,
            userDetails: state => state.auth.userDetails
        })
    },
    methods: {
        ...mapActions('auth', ['update']),
        async updateUser() {
            const isSuccessful = await this.update({
                ...this.userDetails,
                password: this.password,
                passwordConfirmation: this.passwordConfirmation
            })

            if (isSuccessful) {
                this.$swal("Successful", "Your details has been updated", "success")
                this.password = ""
                this.passwordConfirmation = ""
            }
        }
    }
}
</script>

<style>

</style>