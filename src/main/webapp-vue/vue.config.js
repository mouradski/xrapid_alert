module.exports = {
  devServer: {
    disableHostCheck: true,
    port: 8080,
    public: '0.0.0.0:8080'
  },
  publicPath: "/",
  configureWebpack: {
    module: {
      rules: [{ test: /\.hdr$/, use: "url-loader" }]
    },
    externals: {
      // only define the dependencies you are NOT using as externals!
      canvg: "canvg",
      html2canvas: "html2canvas",
      dompurify: "dompurify"
    }
  }
}