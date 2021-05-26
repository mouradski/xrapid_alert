<template>
  <div class="card-wrapper">
    <header>
      <slot name="header"></slot>
    </header>
    <div class="card-content">
      <slot name="content"></slot>
    </div>
  </div>

</template>

<script>
export default {
  name: "Card",
};
</script>

<style scoped lang="scss">
.card-wrapper {
  background: #2b283e;
  padding: 20px;
  border-radius: 10px;
}

@media screen and (max-width: 600px) {
  .card-wrapper {
    padding: 20px 5px;
  }
}

header {
  font-size: 24px;
  font-weight: 600;
  color: #e0e6f1;
}
</style>
