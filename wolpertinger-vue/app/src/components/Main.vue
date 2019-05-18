<template>
  <v-container>
    <v-layout row wrap>
      <v-flex xs6 pa-3>
        <v-img :src="require('./shirt.png')" aspect-ratio="1" class="elevation-3"></v-img>
      </v-flex>
      <v-flex xs6 pa-3>
        <v-layout row wrap>
          <template v-for="image in images">
            <v-flex xs3 :key="image.name">
              <v-img :src="image.high" aspect-ratio="1"></v-img>
            </v-flex>
          </template>
        </v-layout>
      </v-flex>
    </v-layout>
    <v-alert :value="this.error" type="error" dismissable>{{ this.error }}</v-alert>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: "Main",
  data() {
    return {
      imageService: process.env.VUE_APP_BACKEND_URL + "images",
      images: [],
      error: ""
    };
  },
  created() {
    axios
      .get(this.imageService)
      .then(response => {
        this.images = response.data;
      })
      .catch(error => {
        // Error
        if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          this.error = error.response.data;
        } else if (error.request) {
          // The request was made but no response was received
          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
          // http.ClientRequest in node.js
          this.error = error.request;
        } else {
          // Something happened in setting up the request that triggered an Error
          this.error = error.message;
        }
      });
  }
};
</script>