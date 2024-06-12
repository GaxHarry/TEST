const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    allowedHosts: "all"
  },
  transpileDependencies: true
})
