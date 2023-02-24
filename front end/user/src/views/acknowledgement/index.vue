<template>
  <div class="acknowledgement-page">
    <div v-if="'horizontal' === layout" class="index-container">
      <pc-index />
    </div>
    <div v-else class="index-container">
      <cell-phone-index />
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import pcIndex from '@/views/acknowledgement/components/PcAcknowledgement'
  import cellPhoneIndex from '@/views/acknowledgement/components/cellPhoneAcknowledgement'

  export default {
    name: 'PersonalCenter',
    components: {
      pcIndex,
      cellPhoneIndex,
    },
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        layout: 'settings/layout',
      }),
    },
  }
</script>

<style scoped>
  .acknowledgement-page {
    width: 100%;
    height: 900px;
    background-color: black;
  }
</style>
