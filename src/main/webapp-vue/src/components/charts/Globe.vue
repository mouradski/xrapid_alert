<template>
  <div
    class="globe"
    ref="chart"
  ></div>
</template>

<script>
import Card from "@/components/Card.vue";
import * as Three from "three";
import { geoInterpolate } from "d3-geo";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";
import TrackballControls from "three-trackballcontrols";
import { points } from "@/assets/points.json";

export default {
  name: "Globe",
  props: {
    data: {
      type: Array,
    },
  },
  components: {
    Card,
  },
  data() {
    return {
      camera: null,
      scene: null,
      renderer: null,
      mesh: null,
      controls: null,
      weight: [50, 500, 5000, 50000],
      countries: [
        {
          currency: "USD",
          latitude: 42.7392,
          longitude: -85.9902,
          country: "United-States",
        },
        {
          currency: "PHP",
          latitude: 14.6043,
          longitude: 120.9822,
          country: "Philippines",
        },
        {
          currency: "MXN",
          latitude: 23.6345,
          longitude: -102.5527,
          country: "Mexico",
        },
        {
          currency: "AUD",
          latitude: -35.282,
          longitude: 149.1286,
          country: "Australia",
        },
        {
          currency: "THB",
          latitude: 13.7367,
          longitude: 100.5231,
          country: "Thailand",
        },
        {
          currency: "KRW",
          latitude: 37.5326,
          longitude: 127.0246,
          country: "Korea",
        },
        {
          currency: "BRL",
          latitude: -22.9035,
          longitude: -43.2096,
          country: "Brasil",
        },
        {
          currency: "EUR",
          latitude: 47.5101,
          longitude: 8.2055,
          country: "Europe",
        },
        {
          currency: "GBP",
          latitude: 55.5085,
          longitude: -4.9257,
          country: "United Kingdom",
        },
        {
          currency: "JPY",
          latitude: 35.6895,
          longitude: 139.6917,
          country: "Japan",
        },
        {
          currency: "NGN",
          latitude: 9.0777,
          longitude: 8.67745,
          country: "Nigeria",
        },
        {
          currency: "NZD",
          latitude: -36.8484,
          longitude: 174.7633,
          country: "New Zealand",
        },
        {
          currency: "TRY",
          latitude: 39.92077,
          longitude: 32.85411,
          country: "Turkey",
        },
        {
          currency: "ZAR",
          latitude: -28.48322,
          longitude: 24.676997,
          country: "South Africa",
        },
        {
          currency: "SGD",
          latitude: 1.2902,
          longitude: 103.8519,
          country: "Singapore",
        },
        {
          currency: "MYR",
          latitude: 3.1408,
          longitude: 101.6932,
          country: "Malaysia",
        },
      ],
      props: {
        cameraSize: {
          width: (window.innerWidth / 2) * 0.9,
          height: (window.innerHeight / 2) * 0.9,
        },
        mapSize: {
          width: 4098 / 2.1,
          height: 1968 / 2.1,
        },
        globeRadius: 90,
        dotsAmount: 20,
        arcDuration: 1000,
        colours: {
          globeDots: "#1ebc7d",
          countryDots: "#bc1e92",
          lines: new Three.Color("#18FFFF"),
          lineDots: new Three.Color("#18FFFF"),
        },
        alphas: {
          globe: 0.4,
          lines: 0.5,
        },
      },
    };
  },
  computed: {
    paymentsWithCountries() {
      return this.data.map((payment) => {
        (payment.sourceCountry =
          this.countries.find((c) => c.currency === payment.sourceFiat) ||
          false),
          (payment.destinationCountry =
            this.countries.find(
              (c) => c.currency === payment.destinationFiat
            ) || false);
        return payment;
      });
    },
    isMobile() {
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
        navigator.userAgent
      );
    },
  },
  methods: {
    init() {
      const container = this.$refs.chart;

      this.scene = new Three.Scene();
      this.scene.background = null;
      this.addGlobe();
      this.addGlobeDots();

      // Setup camera
      this.camera = new Three.PerspectiveCamera();
      this.camera.aspect =
        this.props.cameraSize.width / this.props.cameraSize.height;
      this.camera.updateProjectionMatrix();
      this.camera.position.z = 500;
      // Setup this.renderer
      this.renderer = new Three.WebGLRenderer({ alpha: true });
      this.renderer.setSize(
        this.props.cameraSize.width,
        this.props.cameraSize.height
      );
      container.appendChild(this.renderer.domElement);

      this.camera.orbitControls = new OrbitControls(
        this.camera,
        this.renderer.domElement
      );
      this.camera.orbitControls.enablePan = false;
      this.camera.orbitControls.enableRotate = false;
      this.camera.orbitControls.autoRotate = true;

      this.controls = new TrackballControls(
        this.camera,
        this.renderer.domElement
      );
      this.controls.minDistance = 250;
      this.controls.maxDistance = 250;
      this.controls.minAzimuthAngle = Math.PI / 2;
      this.controls.maxAzimuthAngle = Math.PI / 2;

      this.controls.rotateSpeed = 5;
      this.controls.zoomSpeed = 0.8;
      this.controls.enabled = !this.isMobile;

      const canvasResizeBehaviour = () => {
        console.log("resizing");
        const { innerWidth, innerHeight } = window;

        container.width = innerWidth / (innerWidth > 1280 ? 2.2 : 1);
        container.height = innerHeight / 2.1;
        console.log(container.width);
        console.log(container.height);
        container.style.width = `${container.width}px`;
        container.style.height = `${container.height}px`;
        this.camera.aspect = container.width / container.height;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(container.width, container.height);
      };

      canvasResizeBehaviour();

      window.addEventListener("resize", canvasResizeBehaviour);
    },

    addGlobe() {
      const textureLoader = new Three.TextureLoader();
      textureLoader.setCrossOrigin(true);
      const radius = this.props.globeRadius - this.props.globeRadius * 0.02;
      const segments = 64;
      const rings = 64;
      const canvasSize = 128;
      const textureCanvas = document.createElement("canvas");
      textureCanvas.width = canvasSize;
      textureCanvas.height = canvasSize;
      const canvasContext = textureCanvas.getContext("2d");
      canvasContext.rect(0, 0, canvasSize, canvasSize);
      const canvasGradient = canvasContext.createLinearGradient(
        0,
        0,
        0,
        canvasSize
      );
      canvasGradient.addColorStop(0, "#2b283e");
      canvasGradient.addColorStop(1, "#2b283e");
      canvasContext.fillStyle = canvasGradient;
      canvasContext.fill();

      const texture = new Three.Texture(textureCanvas);
      texture.needsUpdate = true;

      const geometry = new Three.SphereGeometry(radius, segments, rings);

      const material = new Three.MeshBasicMaterial({
        map: texture,
        transparent: true,
        opacity: 0.8,
      });
      const globeElement = new Three.Mesh(geometry, material);

      this.scene.add(globeElement);
    },
    addGlobeDots() {
      const geometry = new Three.BufferGeometry();
      const canvasSize = 32;
      const halfSize = canvasSize / 2;
      const textureCanvas = document.createElement("canvas");
      textureCanvas.width = canvasSize;
      textureCanvas.height = canvasSize;
      const canvasContext = textureCanvas.getContext("2d");
      canvasContext.beginPath();
      canvasContext.arc(halfSize, halfSize, halfSize, 0, 2 * Math.PI);
      canvasContext.fillStyle = this.props.colours.globeDots;
      canvasContext.fill();

      const texture = new Three.Texture(textureCanvas);
      texture.needsUpdate = true;

      const material = new Three.PointsMaterial({
        map: texture,
        size: this.props.globeRadius / 60,
      });

      let vertices = new Float32Array(
        points
          .map((p) => {
            const result = this.convertFlatCoordsToSphereCoords(p.x, p.y);
            return [result.x, result.y, result.z];
          })
          .reduce((a, b) => a.concat(b), [])
      );

      geometry.setAttribute("position", new Three.BufferAttribute(vertices, 3));

      const globeDotsElement = new Three.Points(geometry, material);
      this.scene.add(globeDotsElement);
    },
    addTransaction(payment) {
      const start = this.countries.find(
        (c) => c.currency === payment.sourceFiat
      );
      const end = this.countries.find(
        (c) => c.currency === payment.destinationFiat
      );

      const weight = this.weight.findIndex((w) => w >= payment.usdValue); //On prend toujours le premier palier qui est au dessus du paiment, on récupère son index pour pondérer.
      if (start && end) {
        this.addArc(start, end, weight);
      }
    },
    addArc(start, end, weight) {
      let startXYZ = this.convertRealCoordsToSphereCoords(
        start.latitude,
        start.longitude
      );

      startXYZ = new Three.Vector3(startXYZ.x, startXYZ.y, startXYZ.z);

      let endXYZ = this.convertRealCoordsToSphereCoords(
        end.latitude,
        end.longitude
      );

      endXYZ = new Three.Vector3(endXYZ.x, endXYZ.y, endXYZ.z);

      const d3Interpolate = geoInterpolate(
        [start.longitude, start.latitude],
        [end.longitude, end.latitude]
      );
      const control1 = d3Interpolate(0.25);
      const control2 = d3Interpolate(0.75);

      const arcHeight =
        startXYZ.distanceTo(endXYZ) * 0.3 +
        (this.props.globeRadius - this.props.globeRadius * 0.02);
      let controlXYZ1 = this.convertRealCoordsToSphereCoords(
        control1[1],
        control1[0],
        arcHeight
      );
      let controlXYZ2 = this.convertRealCoordsToSphereCoords(
        control2[1],
        control2[0],
        arcHeight
      );
      controlXYZ1 = new Three.Vector3(
        controlXYZ1.x,
        controlXYZ1.y,
        controlXYZ1.z
      );
      controlXYZ2 = new Three.Vector3(
        controlXYZ2.x,
        controlXYZ2.y,
        controlXYZ2.z
      );

      const curve = new Three.CubicBezierCurve3(
        startXYZ,
        controlXYZ1,
        controlXYZ2,
        endXYZ
      );

      const geometry = new Three.TubeBufferGeometry(curve, 44, 0.2 * weight, 8);
      const material = new Three.ShaderMaterial({
        uniforms: {
          color1: {
            value: new Three.Color("#9cffd8"),
          },
          color2: {
            value: new Three.Color("#9cffd8"),
          },
        },
        vertexShader: `
        varying vec2 vUv;

        void main() {
          vUv = uv;
          gl_Position = projectionMatrix * modelViewMatrix * vec4(position,1.0);
        }
      `,
        fragmentShader: `
        uniform vec3 color1;
        uniform vec3 color2;
      
        varying vec2 vUv;
        
        void main() {
          
          gl_FragColor = vec4(mix(color1, color2, vUv.y), 1.0);
        }
      `,
        wireframe: true,
      });
      const arcElement = new Three.Mesh(geometry, material);
      this.scene.add(arcElement);

      geometry.setDrawRange(0, 1);
      const startTime = performance.now();
      this.animateLine(geometry, startTime, false);
    },

    animateLine(geometry, startTime, revert) {
      let drawRangeCount = geometry.drawRange.count;
      const timeElapsed = performance.now() - startTime;

      const progress = timeElapsed / this.props.arcDuration;
      if (progress < 0.999) {
        let count,
          start = 0;
        if (revert) {
          start = parseInt(progress * 3000);
          count = geometry.maxDrawRange;
        } else {
          start = 0;
          count = progress * 3000;
        }
        geometry.setDrawRange(start, count);

        requestAnimationFrame(() => {
          this.animateLine(geometry, startTime, revert);
        });
      } else if (progress > 0.999 && !revert) {
        geometry.maxDrawRange = drawRangeCount;
        startTime = performance.now();
        geometry.setDrawRange(1, drawRangeCount);

        requestAnimationFrame(() => {
          this.animateLine(geometry, startTime, true);
        });
      }
    },
    animate() {
      this.controls.update();
      this.camera.orbitControls.update();
      this.renderer.render(this.scene, this.camera);
      requestAnimationFrame(this.animate);
    },
    convertRealCoordsToSphereCoords(lat, lon, radius) {
      if (!radius)
        radius = this.props.globeRadius - this.props.globeRadius * 0.02;
      const phi = (90 - lat) * (Math.PI / 180);
      const theta = (lon + 180) * (Math.PI / 180);
      const x = -(radius * Math.sin(phi) * Math.cos(theta));
      const z = radius * Math.sin(phi) * Math.sin(theta);
      const y = radius * Math.cos(phi);
      return { x, y, z };
    },

    convertFlatCoordsToSphereCoords(latitude, longitude) {
      latitude =
        ((latitude - this.props.mapSize.width) / this.props.mapSize.width) *
        -180;
      longitude =
        ((longitude - this.props.mapSize.height) / this.props.mapSize.height) *
        -90;

      const radius =
        Math.cos((longitude / 180) * Math.PI) * this.props.globeRadius;
      const x = Math.cos((latitude / 180) * Math.PI) * radius;
      const y = Math.sin((longitude / 180) * Math.PI) * this.props.globeRadius;
      const z = Math.sin((latitude / 180) * Math.PI) * radius;

      return { x, y, z };
    },
  },

  mounted() {
    this.init();
    this.animate();
  },
};
</script>

<style scoped lang="scss">
.chart {
  height: 400px;
}
</style>
