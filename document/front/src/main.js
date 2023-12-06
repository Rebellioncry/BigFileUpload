import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import uploader from 'vue-simple-uploader'

const app = createApp(App)
app.use(router)
app.use(uploader)
app.provide('$axios',axios)
app.mount('#app')
