<template>
  <v-container>
    <v-layout row wrap>
      <v-flex xs6 pa-3>
        <v-img :src="require('./shirt.png')" aspect-ratio="1" class="elevation-3 align-center">
          <v-layout row wrap justify-center align-end>
            <v-flex xs1>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.tertiary1" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
            <v-flex xs3>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.main" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
            <v-flex xs1>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.tertiary2" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
          </v-layout>
          <v-layout row wrap justify-center>
            <v-flex xs2>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.secondary1" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
            <v-flex xs2>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.secondary2" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
          </v-layout>
          <v-layout row wrap justify-center>
            <v-flex xs1>
              <v-img :src="circle">
                <v-img v-bind:src="configuration.tertiary3" aspect-ratio="1"></v-img>
              </v-img>
            </v-flex>
          </v-layout>
        </v-img>
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
import circle from "./circle.png";
export default {
  name: "Main",
  data() {
    return {
      circle: circle,
      imageService: process.env.VUE_APP_BACKEND_URL + "images",
      images: [],
      error: "",
      configuration: []
    };
  },
  created() {
    axios
      .get(this.imageService)
      .then(response => {
        this.images = response.data;
        this.configuration = this.loadConfiguration(this.images);
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
  },
  methods: {
    /**
     * Display shirt configuration via url params.
     */
    loadConfiguration(images) {
      var configuration = [];
      const params = new URLSearchParams(window.location.search);
      params.forEach(function(value, key) {
        images.forEach(image => {
          if (image.name == value) {
            configuration[key] = image.high;
          }
        });
      });
      return configuration;
    }
  }
};
</script>