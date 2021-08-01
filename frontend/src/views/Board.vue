<template>
  <v-container
    fluid
  >
    <v-card
      elevation="0"
    >
      <v-card-title>
        All Posts
      </v-card-title>

      <v-card-text>
        <v-simple-table>
          <thead>
            <tr>
              <th 
                class="text-center" 
                width="6%"
              >
                번호
              </th>
              <th 
                class="text-center"
              >
                제목
              </th>
              <th 
                class="text-center" 
                width="10%"
              >
                작성자
              </th>
              <th 
                class="text-center" 
                width="16%"
              >
                날짜
              </th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="(post, no) in posts"
              :key="post.id"
            >
              <td 
                class="text-center"
              >
                {{ no + 1 + ((currentPage - 1) * size) }}
              </td>
              <td
                @click="seeDetails(post)"
              >
                {{ post.title }}
                <span class="font-weight-light">[{{ post.numOfComments }}]</span>
              </td>
              <td 
                class="text-center"
              >
                {{ post.author }}
              </td>
              <td 
                class="text-center"
              >
                {{ post.createdAt }}
              </td>
            </tr>  
          </tbody>
        </v-simple-table>
      </v-card-text>

      <v-card-actions>
        <div
          class="mx-auto"
        >
          <v-pagination
            v-model="currentPage"
            :length="totalPages"
            color="blue-grey darken-4"
            @input="changePage"
          />
        </div>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>    
import { mapState, mapActions } from 'vuex'

export default {
    data () {
        return {
            currentPage: Number.parseInt(this.$route.query.page) || 1
        }
    },
    computed: {
        ...mapState('board', {
            posts: state => state.currentPage.content,
            totalPages: state => state.currentPage.totalPages,
            size: state => state.currentPage.size
        })
    },
    watch: {
        $route: function () {
            this.getPosts(this.currentPage - 1)
        }
    },
    created () {
        this.getPosts(this.currentPage - 1)
    },
    methods: {
        ...mapActions('board', ['getPosts']),
        seeDetails(post) {
            this.$router.push("/post/" + post.id)
        },
        changePage() {
            console.log("query.page = ", this.currentPage)
            this.$router.push("/board?page=" + this.currentPage)
        }
    }
}
</script>

<style>

</style>