<template>
  <div v-if="'horizontal' === layout" class="index-container">
    <pc-index-project ref="pdIndex" />
  </div>
  <div v-else class="index-container">
    <cell-phone-index-project ref="cellPhoneIndex" />
  </div>
</template>

<script>
  import pcIndexProject from '@/views/queryResult/components/PcIndex'
  import cellPhoneIndexProject from '@/views/queryResult/components/cellPhoneIndex'
  import { mapGetters } from 'vuex'

  export default {
    name: 'Index',
    components: {
      pcIndexProject,
      cellPhoneIndexProject,
    },
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        layout: 'settings/layout',
      }),
    },
    mounted() {},
    methods: {},
  }
</script>

<style scoped></style>
