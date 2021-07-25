<template>
  <v-card
    elevation="0    "
  >
    <v-card-title>
      Comments ({{ comments.length }})
    </v-card-title>
    <v-card-text>
      <v-row
        class="px-4"
      >
        <v-textarea
          v-model="newComment"
          background-color="blue-grey darken-4"
          rows="2"
          filled
          no-resize 
          clearable
          dark
          label="Leave a comment"
          append-icon="mdi-checkbox-marked-circle"
          clear-icon="mdi-close-circle"
          @click:append="saveComment"
        />
      </v-row>

      <v-row 
        v-for="comment in comments" 
        :key="comment.id"
        class="px-4"
      >
        <v-col
          class="pb-0"
          cols="12"
        >
          <v-textarea
            v-model="comment.content"
            rows="1"
            auto-grow
            solo
            :readonly="!comment.editable"
            hide-details
          >
            <template 
              v-slot:prepend-inner
            >
              <span
                class="mr-3"
              >
                <v-icon 
                  left                
                >
                  mdi-comment
                </v-icon>
                <b>{{ comment.author }}</b>
              </span>
            </template>
          </v-textarea>  
        </v-col>
        <v-col>
          <template
            v-if="userDetails && comment.authorId === userDetails.id"
          >
            <v-icon 
              v-if="!comment.editable"
              small 
              right
              left
              @click="comment.editable = true"
            >
              mdi-wrench
            </v-icon>
            <template
              v-else
            >
              <v-icon 
                small 
                right
                left
                @click="cancelUpdateComment(comment)"
              >
                mdi-undo
              </v-icon>    
              <v-icon 
                small 
                right
                left
                @click="updateComment(comment)"
              >
                mdi-check-bold
              </v-icon>    
            </template>

            <v-icon 
              small 
              right
              left
              color="red"
              @click="removeComment(comment)"
            >
              mdi-delete
            </v-icon>          
          </template>
          <span>
            Created at: {{ comment.createdAt }}    
            Modified at: {{ comment.modifiedAt }}
          </span>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
    props: {
        postId: {
            type: String,
            required: true      
        }
    },
    data: () => ({
        newComment: ""
    }),
    computed: {
        ...mapState({
            userDetails: state => state.auth.userDetails,
            comments: state => state.comment.comments
        })  
    },
    created () {
        this.getCommentsByPostId(this.postId)
    },
    methods: {
        ...mapActions('comment', ['getCommentsByPostId', 'save', 'update', 'remove']),
        async saveComment () {
            if (this.userDetails === null) {
                this.$swal("Failure", "You need to log in before leaving a comment", "error")
            } else {
                if (this.newComment) {
                    const isSuccessful = await this.save({
                        content: this.newComment,
                        postId: this.postId,
                        userId: this.userDetails.id
                    })

                    if (isSuccessful) {
                        this.getCommentsByPostId(this.postId)
                        this.newComment = ""
                    }
                } else {
                    this.$swal("Failure", "Please enter a comment", "error")
                }
            }
        },
        async updateComment (comment) {
            const isSuccessful = await this.update({
              ...comment,
              userId: comment.authorId
            })

            if (isSuccessful) {
                this.getCommentsByPostId(this.postId)
                comment.editable = false
            }
        },
        cancelUpdateComment (comment) {
            comment.editable = false
            this.getCommentsByPostId(this.postId)
        },
        async removeComment (comment) {
            const isSuccessful = await this.remove(comment.id)

            if (isSuccessful)
                this.getCommentsByPostId(this.postId)
        }
    }
}
</script>

<style>

</style>