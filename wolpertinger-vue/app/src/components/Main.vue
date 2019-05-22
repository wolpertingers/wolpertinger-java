<template>
  <v-container>
    <v-snackbar v-model="snackbar.show" :timeout="snackbar.duration" :top="true">{{ snackbar.text }}</v-snackbar>
    <v-layout row wrap>
      <v-flex xs6 pa-3>
        <v-img :src="require('./shirt.png')" aspect-ratio="1" class="elevation-3 align-center">
          <v-layout row wrap justify-center align-end>
            <v-flex xs1>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="tertiary1"
                    v-bind:src="configuration.tertiary1.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
            <v-flex xs3>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="main"
                    v-bind:src="configuration.main.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
            <v-flex xs1>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="tertiary2"
                    v-bind:src="configuration.tertiary2.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
          </v-layout>
          <v-layout row wrap justify-center>
            <v-flex xs2>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="secondary1"
                    v-bind:src="configuration.secondary1.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
            <v-flex xs2>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="secondary2"
                    v-bind:src="configuration.secondary2.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
          </v-layout>
          <v-layout row wrap justify-center>
            <v-flex xs1>
              <v-img :src="circle">
                <drop @drop="handleDrop">
                  <v-img
                    id="tertiary3"
                    v-bind:src="configuration.tertiary3.high"
                    aspect-ratio="1"
                    @click="setUrlParam($event.currentTarget.id, null)"
                  ></v-img>
                </drop>
              </v-img>
            </v-flex>
          </v-layout>
        </v-img>
        <v-dialog v-model="orderDialog" persistent max-width="600px">
          <template v-slot:activator="{on}">
            <v-btn color="success" :disabled="!button.active" block v-on="on">Place order</v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">Order</span>
            </v-card-title>
            <v-card-text>
              <v-container grid-list-md>
                <v-layout wrap>
                  <v-flex xs12 sm6 md4>
                    <v-text-field id="orderer" label="Name" required></v-text-field>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" flat @click="orderDialog = false">Cancel</v-btn>
              <v-btn color="blue darken-1" flat @click="order">Order</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-flex>
      <v-flex xs6 pa-3>
        <v-layout row wrap>
          <template v-for="image in images">
            <v-flex xs3 :key="image.name">
              <drag v-bind:key="image.name" :transfer-data="image" :drag-image="image.high">
                <v-img :src="image.high" aspect-ratio="1"></v-img>
              </drag>
            </v-flex>
          </template>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";
import { Drag, Drop } from "vue-drag-drop";
import circle from "./circle.png";
export default {
  name: "Main",
  components: {
    Drag,
    Drop
  },
  data() {
    return {
      circle: circle,
      imageService: process.env.VUE_APP_BACKEND_URL + "images",
      orderService: process.env.VUE_APP_BACKEND_URL + "orders",
      images: [],
      configuration: {
        main: "",
        secondary1: "",
        secondary2: "",
        tertiary1: "",
        tertiary2: "",
        tertiary3: ""
      },
      snackbar: {
        duration: 1500,
        show: false,
        text: "nothing"
      },
      button: {
        active: false
      },
      orderDialog: false
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
        this.handleError(error);
      });
  },
  methods: {
    handleError(error) {
      var message = error.message;
      // Error
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        const data = error.response.data;
        message = data.errors
          ? this.formatErrors(data.errors)
          : JSON.stringify(data, null, 2);
      } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        message = error.request;
      }
      this.$dialog.error({
        text: message
      });
    },
    /**
     * Format the errors to nice html.
     */
    formatErrors(errors) {
      var message = "";
      for (var i = 0; i < errors.length; i++) {
        var error = errors[i];
        message += error.message + "\n";
      }
      return message;
    },
    /**
     * Display shirt configuration via url params.
     */
    loadConfiguration(images) {
      const context = this;
      var configuration = {
        main: "",
        secondary1: "",
        secondary2: "",
        tertiary1: "",
        tertiary2: "",
        tertiary3: ""
      };
      var size = 0;
      const params = new URLSearchParams(window.location.search);
      params.forEach(function(value, key) {
        var image = context.getImage(images, value);
        if (!image || context.contains(configuration, image)) {
          window.location.replace(location.pathname);
        }
        configuration[key] = image;
        size++;
      });
      context.button.active = size == 6;
      return configuration;
    },
    getImage(images, name) {
      var found = null;
      images.forEach(image => {
        if (image.name == name) {
          found = image;
        }
      });
      return found;
    },
    handleDrop(image, event) {
      if (!this.hasImage(image.name)) {
        // dragged image unknown
        return;
      }
      if (this.contains(this.configuration, image)) {
        // image already on shirt
        this.showSnackbar("Image already on the shirt.", 1500);
        return;
      }
      const paramName = event.target.offsetParent.id;
      this.setUrlParam(paramName, image.name);
    },
    /**
     * Set query parameter to a given value and reload page.
     */
    setUrlParam(key, value) {
      var params = new URLSearchParams(window.location.search);
      if (value) {
        params.set(key, value);
      } else {
        params.delete(key);
      }
      var newRelativePathQuery =
        window.location.pathname + "?" + params.toString();
      history.pushState(null, "", newRelativePathQuery);
      this.configuration = this.loadConfiguration(this.images);
    },
    /**
     * Check if image with given name exists.
     */
    hasImage(name) {
      var found = false;
      this.images.forEach(image => {
        if (image.name == name) {
          found = true;
        }
      });
      return found;
    },
    contains(array, value) {
      for (var element in array) {
        if (array[element] == value) {
          return true;
        }
      }
      return false;
    },
    /**
     * Show message on top of page.
     */
    showSnackbar(text, duration) {
      this.snackbar.text = text;
      this.snackbar.duration = duration;
      this.snackbar.show = true;
    },
    order() {
      const order = {
        orderer: document.getElementById("orderer").value,
        visible: true,
        images: [
          {
            level: 1,
            image: this.configuration.main
          },
          {
            level: 2,
            image: this.configuration.secondary1
          },
          {
            level: 3,
            image: this.configuration.secondary2
          },
          {
            level: 4,
            image: this.configuration.tertiary1
          },
          {
            level: 5,
            image: this.configuration.tertiary2
          },
          {
            level: 6,
            image: this.configuration.tertiary3
          }
        ]
      };
      axios
        .post(this.orderService, order)
        .then(response => {
          if (response.status == 201) {
            this.showSnackbar(
              "Your Shirt has successfully been ordered!",
              5000
            );
          }
        })
        .catch(error => {
          this.handleError(error);
        });
      this.orderDialog = false;
    }
  }
};
</script>