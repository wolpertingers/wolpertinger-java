import Vue from "vue";
import App from "./App.vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";
import VuetifyDialog from "vuetify-dialog";

Vue.use(Vuetify);
Vue.use(VuetifyDialog);

Vue.config.productionTip = false;

new Vue({
  render: h => h(App)
}).$mount("#app");
