<template>
  <div class="footer">
    <div>
      <span>Conceptor & Developer</span>
      <span>
        <a
          _target="blank"
          href="https://twitter.com/mouradski"
        >
          Mourad TOUMI @mouradski
        </a>
      </span>
    </div>
    <div>
      <span>Analyst & Developer</span>
      <span>
        <a
          _target="blank"
          href="https://twitter.com/0Llac3"
        >
          Labo-a-crypto-3.0 @0Llac3
        </a>
      </span>
    </div>
    <div>
      <span>Front Developer</span>
      <span>
        <a
          _target="blank"
          href="https://twitter.com/Niiitzer"
        >
          Niiitzer @Niiitzer
        </a>
      </span>
    </div>

  </div>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped lang="scss">
.footer {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  margin:30px;

  & > div {
    display: flex;
    margin:20px;
    flex-direction: column;

    & > span:last-of-type{
       margin-top:10px;
    }
  }

  a {
    color: #1ebc7d;
    text-decoration:none;
  }
}
</style>
