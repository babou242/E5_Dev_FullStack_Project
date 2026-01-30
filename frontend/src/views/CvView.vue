<template>
  <div class="cv-container">
    <h1>Mon CV</h1>
    <div class="pdf-wrapper">
      <iframe 
        :src="cvUrl" 
        class="pdf-viewer"
        title="CV"
      ></iframe>
    </div>
    <a :href="cvUrl" download class="download-btn">
      📥 Télécharger le CV
    </a>
  </div>
</template>

<script setup lang="ts">
// Place ton fichier CV.pdf dans le dossier public/
const cvUrl = '/CV.pdf'
</script>

<style scoped>
.cv-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

h1 {
  margin-bottom: 20px;
  color: #333;
}

.pdf-wrapper {
  width: 100%;
  height: 80vh;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.pdf-viewer {
  width: 100%;
  height: 100%;
  border: none;
}

.download-btn {
  display: inline-block;
  margin-top: 20px;
  padding: 12px 24px;
  background-color: #1f6feb;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 600;
  transition: background-color 0.2s;
}

.download-btn:hover {
  background-color: #1558c7;
}
</style>
