<template>
</template>
<script>
  export default {
    name: 'VabGithubCorner',
  }
</script>

<style lang="scss" scoped>
  .github-corner {
    position: absolute;
    top: 0;
    right: 0;
    z-index: $base-z-index - 3;

    .octo-arm {
      animation: octocat-wave 560ms ease-in-out infinite;
    }

    &:hover {
      .octo-arm {
        animation: octocat-wave 560ms ease-in-out infinite;
      }
    }

    .github-color {
      color: #fff;
      fill: $base-color-blue;
    }
  }

  @keyframes octocat-wave {
    0%,
    100% {
      transform: rotate(0);
    }

    20%,
    60% {
      transform: rotate(-25deg);
    }

    40%,
    80% {
      transform: rotate(100deg);
    }
  }
</style>
