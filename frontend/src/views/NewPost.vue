<template>
  <v-container
    fluid
  >
    <v-card
      elevation="0"
    >
      <v-card-title class="mb-4">
        New Post
      </v-card-title>

      <v-card-text>
        <v-text-field 
          v-model="title"
          label="Title"
          dense
        />
        <v-alert
          v-if="validationError.title"
          type="error"
          text
          dense
        >
          {{ validationError.title }}
        </v-alert>

        <editor
          ref="editor"
          height="700px"
        />
        <v-alert
          v-if="validationError.content"
          class="mt-4"
          type="error"
          text
          dense
        >
          {{ validationError.content }}
        </v-alert>
      </v-card-text>

      <v-card-actions>
        <v-spacer />

        <v-btn
          class="blue-grey darken-4 px-4"
          dark
          @click="savePost"
        >
          저장하기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import '@toast-ui/editor/dist/toastui-editor.css'
import 'codemirror/lib/codemirror.css'
import { Editor } from '@toast-ui/vue-editor'
import { mapState, mapActions } from 'vuex'

export default {
    components: {
        'editor': Editor
    },
    data () {
        return {
            title: '',
        }
    },
    computed: {
      ...mapState('error', {
        validationError: state => state.validationError
      })
    },
    methods: {
      ...mapActions('board', ['save']),
      async savePost () {
        const content = this.$refs.editor.invoke('getMarkdown')

        const post = {
          title: this.title, 
          content: content,
          userId: this.$store.state.auth.userDetails.id
        }

        const isSuccessful = await this.save(post)
        
        if (isSuccessful) {
          this.$swal("Successfully,", "The post has been saved", "success")
          .then(() => this.$router.replace("/"))
        }        
      }
    }
}
</script>

<style>

</style>``