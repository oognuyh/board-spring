/* eslint-disable vue/attribute-hyphenation */
<template>
  <v-container
    fluid
  >
    <v-card
      elevation="0"
    >
      <v-card-title>
        # {{ id }}
      </v-card-title>
    
      <v-card-text>
        <div 
          class="mb-7"
        >
          <v-chip 
            class="mr-2 pl-4"
            color="blue-grey darken-4"
            dark
          >
            <v-icon
              left
            >
              mdi-account
            </v-icon>
            {{ post.author }}
          </v-chip>

          <v-chip 
            class="mr-2 pl-4"
            color="blue-grey darken-4"
            dark
          >
            <v-icon
              left
            >
              mdi-pencil
            </v-icon>
            {{ post.createdAt }}
          </v-chip>

          <v-chip 
            class="mr-2 pl-4"
            color="blue-grey darken-4"
            dark
          >
            <v-icon
              left
            >
              mdi-wrench
            </v-icon>
            {{ post.modifiedAt }}
          </v-chip>
        </div>

        <v-text-field 
          v-model="post.title"
          label="Title"
          dense
          :readonly="!isEditable"
        />
        <v-alert
          v-if="validationError.title"
          type="error"
          text
          dense
        >
          {{ validationError.title }}
        </v-alert>

        <div
          v-show="!isEditable"
          style="height: 700px"
        >
          <viewer
            ref="viewer"
          />
        </div>

        <div v-show="isEditable">
          <editor
            ref="editor"
            height="700px"
          />
        </div>
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
      
      <v-divider />

      <v-card-actions>
        <v-btn
          v-show="isEditable"
          class="red pa-4"
          dark
          @click="removePost"
        >
          <v-icon small>
            mdi-delete-circle
          </v-icon>
        </v-btn>
        <v-spacer />

        <template v-if="!isEditable">
          <v-btn
            class="blue-grey darken-1 pa-4"
            dark
            @click="moveToList"
          >
            <v-icon small>
              mdi-arrow-left
            </v-icon>
          </v-btn>
          <v-btn
            class="blue-grey darken-4 pa-4"
            dark
            @click="changeMode"
          >
            <v-icon small>
              mdi-pencil
            </v-icon>
          </v-btn>
        </template>

        <template v-else>
          <v-btn
            class="blue-grey darken-1 pa-4"
            dark
            @click="cancel"
          >
            <v-icon small>
              mdi-arrow-left
            </v-icon>
          </v-btn>

          <v-btn
            class="blue-grey darken-4 pa-4"
            dark
            @click="updatePost"
          >
            <v-icon small>
              mdi-checkbox-marked-circle
            </v-icon>
          </v-btn>
        </template>
      </v-card-actions>
    </v-card>

    <comment 
      v-show="!isEditable"
      :post-id="id"
    />
  </v-container>
</template>

<script>
import '@toast-ui/editor/dist/toastui-editor.css'
import '@toast-ui/editor/dist/toastui-editor-viewer.css';
import { Viewer, Editor } from '@toast-ui/vue-editor'
import { mapState, mapActions } from 'vuex'
import Comment from '@/views/Comment'

export default {
    components: {
        'viewer': Viewer,
        'editor': Editor,
        'comment': Comment
    },
    data: () => ({
        id: null,
        isEditable: false
    }),
    computed: {
        ...mapState({
            post: state => state.board.post,
            validationError: state => state.error.validationError
        }),
    },
    watch: {
        post: function () {  
          this.$refs.viewer.invoke('setMarkdown', this.post.content)
          this.$refs.editor.invoke('setMarkdown', this.post.content)
        }
    },
    created () {
      this.id = this.$route.params.id
    },
    async mounted () {
        await this.getPostById(this.id)
    },
    methods: {
        ...mapActions('board', ['getPostById', 'update', 'remove']),
        moveToList () {        
            this.$router.go(-1)
        },
        changeMode () {
            const user = this.$store.state.auth.userDetails || null
            
            if (user && (user.authorities.some((roles) => roles.authority === "ROLE_ADMIN") || user.id === this.post.userId)) {
              this.isEditable = true
            } else {
              this.$swal('Failure', 'You do not have permisson for this post.', 'error')
            } 
        },
        async updatePost () {
            console.log(this.$refs.editor.invoke('getMarkdown'), this.post)

            const isSuccessful = await this.update({
              ...this.post,
              content: this.$refs.editor.invoke('getMarkdown'),
            })

            if (isSuccessful) {
              this.$swal('Successfully', 'The post has been updated.', 'success')
              .then(() => {
                this.isEditable = false
                this.getPostById(this.id)
              })
            }
        },
        cancel () {
           this.isEditable = false
           this.getPostById(this.id)
        },
        async removePost () {
            const isSuccessful = await this.remove(this.post.id)

            if (isSuccessful) {
                this.$swal('Successfully', 'The post has been removed.', 'success')
                .then(() => this.$router.replace('/'))
            }
        }
    },
    
}
</script>

<style>

</style>