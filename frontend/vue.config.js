module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  outputDir: '../src/main/resources/static',
  devServer: {
    port: 9998,
    proxy: {
      '/api': {
        target: 'http://localhost:9999'
      }
    }
  }
}
