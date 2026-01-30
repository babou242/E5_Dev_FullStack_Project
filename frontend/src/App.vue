<template>
  <div class="app">
    <NavBar />
    <main class="container">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.container {
  width: 100%;
  margin: 0 auto;
  padding: 18px 0;
}
</style>
